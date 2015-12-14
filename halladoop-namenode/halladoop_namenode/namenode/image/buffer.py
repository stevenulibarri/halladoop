"""
Represents the moving parts of the system that haven't been finalized and therefore are not in the
image aka virtual file system
"""
import time
from queue import Queue


class ActionBuffer:

    def __init__(self):
        self.deletes_in_progress = {}
        self.replications_in_progress = {}
        self.queued_deletions = {}
        self.queued_replications = {}
        self.deletion_queue = Queue()
        self.replication_queue = Queue()

    def add(self, node_id, block_id, buffer_dict):
        if node_id not in buffer_dict:
            buffer_dict[node_id] = {}
        entry = BlockEntry()
        buffer_dict[node_id][block_id] = entry

        if buffer_dict == self.queued_replications:
            self.replication_queue.put(entry)
        elif buffer_dict == self.queued_deletions:
            self.deletion_queue.put(entry)

    def block_exists(self, node_id, block_id, buffer_dict):
        return node_id in buffer_dict and block_id in buffer_dict[node_id]

    def remove_if_exists(self, node_id, block_id, buffer_dict):
        if self.block_exists(node_id, block_id, buffer_dict):
            buffer_dict[node_id].pop(block_id)

    def get_next_deletion(self):
        return self._get_next(self.deletes_in_progress, self.deletion_queue)

    def get_next_replication(self):
        return self._get_next(self.replications_in_progress, self.replication_queue)

    def _get_next(self, buffer_dict, buffer_queue):
        next_entry = None
        while not next_entry and not buffer_queue.empty():
            next_entry = buffer_queue.get()
            if next_entry not in buffer_dict:
                next_entry = None

        return next_entry


class BlockEntry:

    def __init__(self):
        self.time_issued = time.localtime()
