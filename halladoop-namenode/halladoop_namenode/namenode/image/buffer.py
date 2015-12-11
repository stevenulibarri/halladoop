"""
Represents the moving parts of the system that haven't been finalized and therefore are not in the
image aka virtual file system
"""
import time

class ActionBuffer:
    def __init__(self):
        self.deletes_in_progress = {}
        self.replications_in_progress = {}
        self.queued_deletions = {}
        self.queued_replications = {}

    def add(self, node_id, block_num, buffer_dict):
        if node_id not in buffer_dict:
            buffer_dict[node_id] = {}
        buffer_dict[node_id][block_num] = BlockEntry()

    def block_exists(self, node_id, block_num, buffer_dict):
        return node_id in buffer_dict and block_num in buffer_dict[node_id]

    def remove_if_exists(self, node_id, block_num, buffer_dict):
        if self.block_exists(node_id, block_num, buffer_dict):
            buffer_dict[node_id].pop(block_num)

class BlockEntry:
    def __init__(self):
        self.time_issued = time.localtime()
