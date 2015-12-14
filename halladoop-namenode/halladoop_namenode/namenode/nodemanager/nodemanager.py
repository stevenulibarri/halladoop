from namenode.nodemanager.datanode import DataNode
import random


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
        if node_id >= 0 and node_id < len(self.nodes):
            self.nodes[node_id].update(available_space_mb)
        else:
            raise ValueError("Node with id " + str(node_id) + " is not registered")

    def get_ips_for_nodes(self, node_ids):
        ips = []
        for node_id in node_ids:
            if node_id in self.nodes:
                ips.append(self.nodes[node_id].node_ip)
        return ips

    def get_nodes_for_write(self, num_nodes):
        nodes = random.sample(self.nodes, num_nodes)
        ips = (n.node_ip for n in nodes)
        return ips
