
class RegistrationRequest(object):

    def __init__(self, nodeIP, totalDiskSpaceMb, availableDiskSpaceMB, **kwargs):
        self.nodeIP = nodeIP
        self.totalDiskSpaceMb = totalDiskSpaceMb
        self.availableDiskSpaceMB = availableDiskSpaceMB


class Heartbeat(object):

    def __init__(self, nodeID, availableDiskSpaceMB, blockManifest, **kwargs):
        self.nodeID = nodeID
        self.availableDiskSpaceMB = availableDiskSpaceMB
        self.blockManifest = blockManifest


class FinalizeRequest(object):

    def __init__(self, blockID, nodes, **kwargs):
        self.blockID = blockID
        self.nodes = nodes


class WriteRequest(object):

    def __init__(self, path, numBlocks, **kwargs):
        self.path = path
        self.numBlocks = numBlocks
