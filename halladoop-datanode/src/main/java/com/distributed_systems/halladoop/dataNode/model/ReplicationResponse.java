package com.distributed_systems.halladoop.dataNode.model;

public class ReplicationResponse {

	private String blockID;
	private String[] nodes;
	
	public String getBlockID() {
		return blockID;
	}
	public void setBlockID(String blockID) {
		this.blockID = blockID;
	}
	public String[] getNodes() {
		return nodes;
	}
	public void setNodes(String[] nodes) {
		this.nodes = nodes;
	}
	
	
}
