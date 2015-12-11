"""
Used for checking validity of data node manifests against virtual file system manifests
"""

def check_match(supposed_manifest, vfs_manifest):
    actual_manifest = __vfs_manifest_blocks__(vfs_manifest)
    supposed_manifest_set = set(supposed_manifest)
    actual_manifest_set = set(actual_manifest)
    nodes_mismatch = list(supposed_manifest_set - actual_manifest_set)
    vfs_mismatch = list(actual_manifest_set - supposed_manifest_set)

    return nodes_mismatch, vfs_manifest

def __vfs_manifest_blocks__(vfs_manifest):
    return list(vfs_manifest.values())
