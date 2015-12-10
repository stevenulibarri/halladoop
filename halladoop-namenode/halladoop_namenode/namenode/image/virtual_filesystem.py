"""
Represents the "journal" of a filesystem similarly to what you'd expect int an ext# filesystem
#TODO
  - timestamps
  - implement a buffer for adding an entry
  - implement some sort of time mechanism to assist heartbeats, if applicable
  - optimization of data structure (which I'm sure is possible)
"""
import config

class VirtualFileSystem:
    def __init__(self):
        self.root_inode = INode(config.DELIMITER, is_directory=True)
        self.data_nodes = {}

    def add_file(self, file_path): #TODO recursive
        inode = self.__add_inode__(file_path) 

    def add_directory(self, file_path):
        self.__add_inode__(file_path)

    def add_block_entry(self, file_path, file_block_num, data_node_id):
        inode = self.__get_inode__(file_path)
        if inode and not inode.is_directory:
            inode.add_pointer(file_block_num, data_node_id)
            self.__add_data_node_entry__(data_node_id, file_path, file_block_num)
        else:
            raise ValueError("INode at " + file_path + " either doesn't exist or isn't a file INODE")

    def get_files_for_data_node(self, data_node_id):
        return self.data_nodes[data_node_id]

    #TODO fix
    def __add_data_node_entry__(self, data_node_id, file_path, file_block_num):
        if data_node_id not in self.data_nodes:
            data_node_entries = {}
            self.data_nodes[data_node_id] = data_node_entries

        file_block_nums = data_nodes_entries[file_path]
        if not file_block_nums:
            file_block_nums = []
            data_node_entries[file_path] = file_block_nums

        if file_block_num not in file_block_nums:
            file_block_nums.append(file_block_num)

    def __get_inode__(self, file_path):
        current_node = self.root_inode
        dirs = list(filter(('').__ne__, file_path.split(config.DELIMITER)))
        for dir_name in dirs:
            if current_node.is_directory:
                current_node = current_node.pointers[dir_name]
                if not current_node:
                    dirs.clear() # current_node doesn't exist, file_path not valid, break loop
            else:
                current_node = None
                dirs.clear() #stop loop

        return current_node

    def __add_inode__(self, file_path): #TODO make recursively add if parent dirs don't exist
        parent_inode = self.__parentinode__(file_path)
        self.__check_parent_inode__(parent_inode)

        inode = INode.fromFileName(self.__filename__(file_path))
        
        parent_inode.add_pointer(inode.file_name, inode)
        return inode
        
    def __filename__(self, file_path):
        return file_path.split(config.DELIMITER)[-1]

    def __parentinode__(self, file_path):
        parent_path = self.__parentpath__(file_path)
        return self.__get_inode__(parent_path)

    def __parentpath__(self, file_path):
        parent_dirs = [self.root_inode.file_name]
        parent_dirs.extend(file_path.split(config.DELIMITER)[1:])
        parent_path = "".join(parent_dirs[:-1])
        return parent_path
        
    def __check_parent_inode__(self, parent_inode):
        if not parent_inode:
            raise ValueError("No parent directory above " + file_path)
        elif not parent_inode.is_directory:
            raise ValueError("Parent node is not a directory")  

"""
A "pointer" is a dictionary for each INode such that
    INode is directory: key=child INode file name, value=child INode
    INode is not directory: key=block number, value=a DataNodePointer
"""
class INode:
    def __init__(self, file_name, is_directory=True, **pointers): 
        self.file_name = file_name
        self.is_directory = is_directory
        self.timestamp = None #TODO
        self.pointers = pointers
    
    @classmethod
    def fromFileName(cls, file_name):
        return cls(file_name, {})

    def add_pointer(self, pointer_key, pointer_value):
        if pointer_key in self.pointers:
            if not self.is_directory:
                pointer = self.pointers[pointer_key]
                pointer.append(pointer_value)
        else:
            if self.is_directory:
                self.pointers[pointer_key] = pointer_value
            else:
                pointer = [pointer_value]
                self.pointers[pointer_key] = pointer
