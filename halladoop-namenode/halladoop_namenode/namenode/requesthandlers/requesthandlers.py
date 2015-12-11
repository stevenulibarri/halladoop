from namenode.models import responsemodels
from namenode.nodemanager import nodemanager
from namenode.image.virtualfilesystem import VirtualFileSystem
from namenode.image import manifestcomparator as manifests

node_manager = nodemanager.NodeManager()
vfs = VirtualFileSystem()

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

    #Check block_manifest against VFS
    datanode_mismatch_blocks, vfs_mismatch_blocks = manifests.check_match(node_manifest, vfs.get_blocks_for_node(node_id))
    print("Datanode mismatch blocks " + str(datanode_mismatch_blocks))
    print("VFS mismatch blocks " + str(vfs_mismatch_blocks))
    #if node has blocks that VFS doesn't
    #        add to delete response
    #        add delete to in progress buffer
    #if vfs has blocks that vfs doesn't
    #    check in progress buffer, if not in buffer
    #        add to replicate response
    #        add replicate to progress buffer

    return responsemodels.HeartbeatResponse(["1", "2", "3"], [{"block_id": "123", "nodes": ["1.2.3.4"]}])


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
