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

    def get_inode(self, file_path):
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

    def get_inodes_for_data_node(self, data_node_id):
        return self.data_nodes[data_node_id]

    def add_inode(self, file_path):
        parent_inode = self.__parentinode__(file_path)
        self.__check_parent_inode__(parent_inode)

        inode = INode.fromFileName(self.__filename__(file_path))
        
        parent_inode.add_pointer(inode_pointer)
        return inode
        
    #TODO add nodes to data_nodes dict if necessary
    def add_file(self, file_path):
        inode = self.add_inode(file_path) 

    def add_directory(self, file_path):
        self.add_inode(file_path)

    def __filename__(self, file_path):
        return file_path.split(config.DELIMITER)[-1]

    def __parentinode__(self, file_path):
        parent_path = self.__parentpath__(file_path)
        return self.get_inode(parent_path)

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

    def add_pointer_from_data(self, file_block_num, data_node_id, data_node_block_id):
        data_node_pointer = DataNodePointer(data_node_id, data_node_block_id)

        file_block = self.pointers[file_block_num]
        if not file_block:
            file_block = FileBlock()
            self.pointers[file_block_num] = file_block

        file_block.add_pointer(data_node_pointer)

class FileBlock:
    def __init__(self):
        self.data_pointers = []

    def add_pointer(self, data_node_pointer):
        self.data_pointers.append(data_node_pointer)

# "Points" to the data on a data node
class DataNodePointer:
    def __init__(self, data_node_id, block_id):
        self.data_node_id = data_node_id
        self.block_id = block_id
