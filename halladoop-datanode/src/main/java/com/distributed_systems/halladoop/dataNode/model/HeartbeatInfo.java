package com.distributed_systems.halladoop.dataNode.model;

import java.util.List;
import java.util.Set;

public class HeartbeatInfo {

	private String nodeID;
	private Set<String> blockManifest;
	private long availableDiskSpace;
	
	public String getNodeID() {
		return nodeID;
	}
	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}
	public Set<String> getBlockManifest() {
		return blockManifest;
	}
	public void setBlockManifest(Set<String> blockManifest) {
		this.blockManifest = blockManifest;
	}
	public long getAvailableDiskSpace() {
		return availableDiskSpace;
	}
	public void setAvailableDiskSpace(long availableDiskSpace) {
		this.availableDiskSpace = availableDiskSpace;
	}
	
	
}
