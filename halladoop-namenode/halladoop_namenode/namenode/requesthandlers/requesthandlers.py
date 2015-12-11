from namenode.models import responsemodels
from namenode.nodemanager import nodemanager

node_manager = nodemanager.NodeManager()


def handle_register(registration_request):
    node_ip = registration_request.node_ip
    total_space_mb = registration_request.total_disk_space_mb
    available_space_mb = registration_request.available_disk_space_mb

    new_id = node_manager.register_node(node_ip, total_space_mb, available_space_mb)

    return responsemodels.RegistrationResponse(new_id)


def handle_heartbeat(heartbeat):
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
