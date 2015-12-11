"""
Used for checking validity of data node manifests against virtual file system manifests
"""

def check_match(supposed_manifest, vfs_manifest):
    supposed_manifest_set = set(supposed_manifest)
    nodes_mismatch = list(supposed_manifest_set - vfs_manifest)
    vfs_mismatch = list(vfs_manifest - supposed_manifest_set)

    return nodes_mismatch, vfs_manifest
