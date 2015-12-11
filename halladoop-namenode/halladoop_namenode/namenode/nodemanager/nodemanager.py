from namenode.nodemanager.datanode import DataNode


class NodeManager():

    def __init__(self):
        self.id = 0
        self.nodes = []

    def register_node(self, node_ip, total_space_mb, available_space_mb):
        new_node_id = self.id
        new_node = DataNode(new_node_id, node_ip, total_space_mb, available_space_mb)
        self.nodes.append(new_node)
        self.id = self.id + 1
        return new_node_id

    def update_node(self, node_id, available_space_mb):
        self.nodes[node_id].update(available_space_mb)
