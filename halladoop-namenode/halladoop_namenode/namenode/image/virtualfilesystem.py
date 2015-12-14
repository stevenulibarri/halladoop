"""
Represents the "journal" of a filesystem similarly to what you'd expect int an ext# filesystem
#TODO
  - timestamps
"""
import time
from threading import RLock

from namenode import config

lock = RLock()

class VirtualFileSystem:
    def __init__(self):
        self._root_inode = INode(config.DELIMITER, is_directory=True)
        self._data_nodes = {}

    def add_file(self, file_path): 
        return self._add_inode(file_path, False)

    def add_directory(self, file_path):
        return self._add_inode(file_path, True)

    def _add_inode(self, file_path, is_directory):
        with lock:
            parent_inode = self._parentinode(file_path)
            if not parent_inode:
                self.add_directory(self._parentpath(file_path))
                parent_inode = self._parentinode(file_path)
            else:
                self._check_parent_inode(parent_inode)
    
            inode = INode(file_path, is_directory)
            
            parent_inode.add_pointer(inode.file_name, inode)
        return inode        

    def _filename(self, file_path):
        return file_path.split(config.DELIMITER)[-1]

    def _parentinode(self, file_path):
        parent_path = self._parentpath(file_path)
        return self._get_inode(parent_path)

    def _parentpath(self, file_path):
        parent_dirs = []
        parent_dirs.extend(file_path.split(config.DELIMITER)[1:])
        parent_path = config.DELIMITER + config.DELIMITER.join(parent_dirs[:-1])
        return parent_path

    def _check_parent_inode(self, parent_inode):
        if not parent_inode.is_directory:
            raise ValueError("Parent node is not a directory")

    def add_block_entry(self, node_id, block_id):
        file_path, block_num = self.parse_block_id(block_id)
        with lock:
            inode = self._get_inode(file_path)
            if not inode:
                inode = self.add_file(file_path)

            if not inode.is_directory:
                inode.add_pointer(block_num, node_id)
                self._add_data_node_entry(node_id, file_path, block_num)
            else:
                raise ValueError("INode at " + file_path + " either doesn't exist or isn't a file INode")

    def remove_block_entry(self, node_id, block_id):
        file_path, block_num = self.parse_block_id(block_id)
        with lock:
            inode = self._get_inode(file_path)
            if inode and not inode.is_directory:
                if block_num in inode.pointers:
                    inode.pointers.pop(block_num, node_id)

    def get_blocks_for_node(self, data_node_id):
        lock.acquire()
        lock.release()
        blocks = set()

        if data_node_id in self._data_nodes:
            blocks = self._data_nodes[data_node_id]

        return blocks

    def get_nodes_for_block(self, block_id):
        file_path, block_num = self.parse_block_id(block_id)
        inode = self._get_inode(file_path)

        nodes_with_block = set()
        if block_num in inode.pointers:
            nodes_with_block.update(inode.pointers[block_num])

        return nodes_with_block

    def get_blocks_for_file(self, file_path):
        inode = self._get_inode(file_path)
        blocks = []

        if inode and inode.is_directory:
            for block_num, node_ids in inode.pointers.items():
                entry = {"block_id": file_path + str(block_num)}
                entry["nodes"] = node_ids
                blocks.append(entry)

    def parse_block_id(self, block_id):
        block_id = str(block_id)
        file_name = block_id
        number_string = []

        for char in reversed(file_name):
            file_name = file_name[:-1]
            if char.isdigit():
                number_string.append(char)
            else:
                file_name += char
                break

        block_num = ''.join(number_string[::-1])
        return file_name, block_num

    def file_exists(self, file_path):
        return self._get_inode(file_path) is not None

    def _add_data_node_entry(self, data_node_id, file_path, file_block_num):
        if data_node_id not in self._data_nodes:
            data_node_entries = set()
            self._data_nodes[data_node_id] = data_node_entries
        else:
            data_node_entries = self._data_nodes[data_node_id]

        block_id = file_path + str(file_block_num)

        if block_id not in data_node_entries:
            data_node_entries.add(block_id)

    def _get_inode(self, file_path):
        lock.acquire()
        lock.release()
        current_node = self._root_inode
        current_node_path = ""
        dirs = list(filter(('').__ne__, file_path.split(config.DELIMITER)))
        for dir_name in dirs:
            dir_name = current_node_path + config.DELIMITER + dir_name
            if current_node.is_directory:
                if dir_name in current_node.pointers:
                    current_node = current_node.pointers[dir_name]
                    current_node_path = current_node.file_name
                else:
                    current_node = None
                    dirs.clear()  # current_node doesn't exist, file_path not valid, break loop
            else:
                current_node = None
                dirs.clear()  # stop loop

        return current_node

    def __str__(self):
        return self._get_string(self._root_inode, 0)

    def _get_string(self, node, space_level):
        if not node.is_directory:
            ret = str(" " * space_level)
            return str(ret + str(node))
        else:
            ret = (" " * space_level) + str(node)
            for child in node.pointers.values():
                ret += "\n" + str((" " * (space_level + 1))) + str(self._get_string(child, space_level + 1))
            return ret
"""
A "pointer" is a dictionary for each INode such that
    INode is directory: key=child INode file name, value=child INode
    INode is not directory: key=block number, value=a DataNodePointer
"""
class INode:
    def __init__(self, file_name, is_directory=True, **pointers): 
        self.file_name = file_name
        self.is_directory = is_directory
        self.timestamp = time.localtime()
        self.pointers = pointers

    def add_pointer(self, pointer_key, pointer_value):
        if pointer_key in self.pointers:
            if not self.is_directory:
                pointer = self.pointers[pointer_key]
                pointer.add(pointer_value)
        else:
            if self.is_directory:
                self.pointers[pointer_key] = pointer_value
            else:
                pointer = set([pointer_value])
                self.pointers[pointer_key] = pointer

    def __str__(self):
        ret = "D " if self.is_directory else "F "
        ret += self.file_name
        ret += " " + str(self.timestamp)
        return ret
