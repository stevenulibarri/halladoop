from namenode.models import responsemodels
from namenode.nodemanager import nodemanager
from namenode.image.virtualfilesystem import VirtualFileSystem
from namenode.image import manifestcomparator as manifests
from namenode.image.buffer import ActionBuffer
from namenode import config
import logging

logger = logging.getLogger('namenode')

node_manager = nodemanager.NodeManager()
vfs = VirtualFileSystem()
buffer = ActionBuffer()

def handle_register(registration_request):
    node_ip = registration_request.node_ip
    total_space_mb = registration_request.total_disk_space_mb
    available_space_mb = registration_request.available_disk_space_mb

    new_id = node_manager.register_node(node_ip, total_space_mb, available_space_mb)

    return responsemodels.RegistrationResponse(new_id)

def handle_heartbeat(heartbeat):
    node_id = heartbeat.node_id
    available_disk_space_mb = heartbeat.available_disk_space_mb
    node_manifest = heartbeat.block_manifest

    node_manager.update_node(node_id, available_disk_space_mb)

    datanode_mismatch_blocks, vfs_mismatch_blocks = manifests.check_match(node_manifest, vfs.get_blocks_for_node(node_id))
    delete_response_blocks = _get_delete_response(node_id, datanode_mismatch_blocks)
    replicate_response_blocks = _get_replicate_response(node_id, vfs_mismatch_blocks)

    logger.info("Datanode mismatch blocks " + str(delete_response_blocks))
    logger.info("VFS mismatch blocks " + str(replicate_response_blocks))
    for block in vfs_mismatch_blocks:
        vfs.remove_block_entry(node_id, block)

    return responsemodels.HeartbeatResponse(delete_response_blocks, replicate_response_blocks)

def _get_delete_response(node_id, mismatched_blocks):
    delete_response = []
    
    for block in mismatched_blocks:
        if buffer.block_exists(node_id, block, buffer.deletes_in_progress):
            block_entry_time = buffer.deletes_in_progress[node_id][block].time_issued
            logger.info("Delete was issued for block " + str(block) + " on node " + str(node_id) + ": " + str(block_entry_time))
        else:
            logger.info("Block " + str(block) + " needs to be deleted in node " + str(node_id))
            buffer.remove_if_exists(node_id, block, buffer.queued_deletions)
            buffer.add(node_id, block, buffer.deletes_in_progress)
            vfs.remove_block_entry(node_id, block)
            delete_response.append(block)
            
    return delete_response

def _get_replicate_response(node_id, mismatched_blocks):
    replicate_response_blocks = []

    if node_id not in buffer.replications_in_progress:
        for mismatched_block in mismatched_blocks:
            if buffer.block_exists(node_id, mismatched_block, buffer.replications_in_progress):
                block_entry_time = buffer.replications_in_progress[node_id][mismatched_block].time_issued
                logger.info("Time replicate was issued for block " + str(mismatched_block) + " on node "
                      + node_id + ": " + str(block_entry_time))
            else:
                logger.info("Block " + str(mismatched_block) + " needs to be replicated in node " + str(node_id))
                buffer.remove_if_exists(node_id, mismatched_block, buffer.queued_replications)
                buffer.add(node_id, mismatched_block, buffer.replications_in_progress)
                replicate_response_blocks.append(mismatched_block)

        extra_replicate_block = buffer.get_next_replication()
        if extra_replicate_block:
            buffer.add(node_id, extra_replicate_block, buffer.replications_in_progress)
            replicate_response_blocks.append(extra_replicate_block)


    replicate_response = []
    for mismatched_block in replicate_response_blocks:
        mismatched_block_entry = {"block_id": mismatched_block}
        nodes_with_mismatched_block = vfs.get_nodes_for_block(mismatched_block)
        mismatched_block_entry["nodes"] = nodemanager.get_ips_for_nodes(nodes_with_mismatched_block)
        replicate_response.append(mismatched_block_entry)

    return replicate_response

def handle_finalize(finalize_request):

    block_id = finalize_request.block_id
    nodes = finalize_request.nodes

    for node_id in nodes:
        buffer.remove_if_exists(node_id, block_id, buffer.replications_in_progress)
        vfs.add_block_entry(block_id, node_id)

def handle_write(write_request):
    nodes = nodemanager.get_nodes_for_write(config.REPLICATION_FACTOR)
    return responsemodels.WriteResponse(nodes)

def handle_read(file_path):
    return responsemodels.ReadResponse([
            {"block_id": "123", "nodes": ["1.2.3.4.", "4.3.2.1", "8.8.8.8"]},
            {"block_id": "321", "nodes": ["1.2.3.4.", "4.3.2.1", "8.8.8.8"]},
        ])

def handle_delete(file_path):
    pass

def cluster_query():
    return {"nodes": node_manager.nodes}
