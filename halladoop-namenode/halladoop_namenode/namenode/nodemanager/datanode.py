import time


class DataNode:

    def __init__(self, node_id, node_ip, total_space_mb, available_space_mb):

        self.node_id = node_id
        self.node_ip = node_ip
        self.total_space_mb = total_space_mb
        self.available_space_mb = total_space_mb
        self.last_reported = time.ctime()
