package com.distributed_systems.halladoop.dataNode.model;

import java.util.Set;

public class HeartbeatInfo {

	private int node_id;
	private Set<String> block_manifest;
	private long available_disk_space_mb;
	
	public int getnode_id() {
		return node_id;
	}
	public void setnode_id(int i) {
		this.node_id = i;
	}
	public Set<String> getblock_manifest() {
		return block_manifest;
	}
	public void setblock_manifest(Set<String> blockManifest) {
		this.block_manifest = blockManifest;
	}
	public long getavailable_disk_space_mb() {
		return available_disk_space_mb;
	}
	public void setavailable_disk_space_mb(long availableDiskSpace) {
		this.available_disk_space_mb = availableDiskSpace;
	}
	
	
}
