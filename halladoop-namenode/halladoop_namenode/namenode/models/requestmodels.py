
class RegistrationRequest(object):

    def __init__(self, node_ip, total_disk_space_mb, available_disk_space_mb, **kwargs):
        self.node_ip = node_ip
        self.total_disk_space_mb = total_disk_space_mb
        self.available_disk_space_mb = available_disk_space_mb


class Heartbeat(object):

    def __init__(self, node_id, available_disk_space_mb, block_manifest, **kwargs):
        self.node_id = node_id
        self.available_disk_space_mb = available_disk_space_mb
        self.block_manifest = block_manifest


class FinalizeRequest(object):

    def __init__(self, block_id, nodes, **kwargs):
        self.block_id = block_id
        self.nodes = nodes


class WriteRequest(object):

    def __init__(self, file_path, num_blocks, **kwargs):
        self.file_path = file_path
        self.num_blocks = num_blocks
