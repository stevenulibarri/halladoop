package com.distributed_systems.halladoop.dataNode.model;

import java.util.List;
import java.util.Set;

public class HeartbeatInfo {

	private String node_id;
	private Set<String> block_manifest;
	private long available_disk_space;
	
	public String getNodeID() {
		return node_id;
	}
	public void setNodeID(String nodeID) {
		this.node_id = nodeID;
	}
	public Set<String> getBlockManifest() {
		return block_manifest;
	}
	public void setBlockManifest(Set<String> blockManifest) {
		this.block_manifest = blockManifest;
	}
	public long getAvailableDiskSpace() {
		return available_disk_space;
	}
	public void setAvailableDiskSpace(long availableDiskSpace) {
		this.available_disk_space = availableDiskSpace;
	}
	
	
}
