from namenode.models import responsemodels
from namenode.nodemanager import nodemanager
from namenode.image.virtualfilesystem import VirtualFileSystem
from namenode.image import manifestcomparator as manifests
from namenode.image.buffer import ActionBuffer

node_manager = nodemanager.NodeManager()
vfs = VirtualFileSystem()
buffer = ActionBuffer()

def handle_register(registration_request):
    node_ip = registration_request.node_ip
    total_space_mb = registration_request.total_disk_space_mb
    available_space_mb = registration_request.available_disk_space_mb

    new_id = node_manager.register_node(node_ip, total_space_mb, available_space_mb)

    return responsemodels.RegistrationResponse(new_id)

#TODO remove from buffer if data node has deleted something
def handle_heartbeat(heartbeat):
    node_id = heartbeat.node_id
    available_disk_space_mb = heartbeat.available_disk_space_mb
    node_manifest = heartbeat.block_manifest

    node_manager.update_node(node_id, available_disk_space_mb)

    datanode_mismatch_blocks, vfs_mismatch_blocks = manifests.check_match(node_manifest, vfs.get_blocks_for_node(node_id))
    print("Datanode mismatch blocks " + str(datanode_mismatch_blocks))
    print("VFS mismatch blocks " + str(vfs_mismatch_blocks))

    delete_response_blocks = []
    replicate_response_blocks = []

    for mismatched_block in datanode_mismatch_blocks:
        if buffer.block_exists(node_id, mismatched_block, buffer.deletes_in_progress):
            block_entry_time = buffer.deletes_in_progress[node_id][mismatched_block].time_issued
            print("Delete was issued for block " + str(mismatched_block) + " on node " + str(node_id) + ": " + str(block_entry_time))
        else:
            print("Block " + str(mismatched_block) + " needs to be deleted in node " + str(node_id))
            buffer.remove_if_exists(node_id, mismatched_block, buffer.queued_deletes)
            buffer.add(node_id, mismatched_block, buffer.deletes_in_progress)
            delete_response_blocks.append(mismatched_block)

    for mismatched_block in vfs_mismatch_blocks:
        if buffer.block_exists(node_id, mismatched_block, buffer.replications_in_progress):
            block_entry_time = buffer.replications_in_progress[node_id][mismatched_block].time_issued
            print("Time replicate was issued for block " + str(mismatched_block) + " on node " + node_id + ": " + str(block_entry_time))
        else:
            print("Block " + str(mismatched_block) + " needs to be replicated in node " + str(node_id))
            buffer.remove_if_exists(node_id, mismatched_block, buffer.queued_replications)
            buffer.add(node_id, mismatched_block, buffer.replications_in_progress)
            replicate_response_blocks.append(mismatched_block)

    #TODO add any other replications that might need to happen

    return responsemodels.HeartbeatResponse(delete_response_blocks, [{"block_id": "123", "nodes": ["1.2.3.4"]}])


def handle_finalize(finalize_request):
    pass


def handle_write(write_request):
    return responsemodels.WriteResponse(["1.1.1.1", "2.2.2.2"])


def handle_read(file_path):
    return responsemodels.ReadResponse([
            {"block_id": "123", "nodes": ["1.2.3.4.", "4.3.2.1", "8.8.8.8"]},
            {"block_id": "321", "nodes": ["1.2.3.4.", "4.3.2.1", "8.8.8.8"]},
        ])


def handle_delete(file_path):
    pass


def cluster_query():
    return {"nodes": node_manager.nodes}
