
class RegistrationResponse(object):

    def __init__(self, nodeID):
        self.nodeID = nodeID


class HeartbeatResponse(object):

    def __init__(self, delete, replicate):
        self.delete = delete
        self.replicate = replicate


class WriteResponse(object):

    def __init__(self, nodes):
        self.nodes = nodes


class ReadResponse(object):

    def __init__(self, manifest):
        self.manifest = manifest
