package com.distributed_systems.halladoop.dataNode.model;

public class ReplicationResponse {

	private String block_id;
	private String[] nodes;
	
	public String getBlock_id() {
		return block_id;
	}
	public void setBlock_id(String block_id) {
		this.block_id = block_id;
	}
	public String[] getNodes() {
		return nodes;
	}
	public void setNodes(String[] nodes) {
		this.nodes = nodes;
	}
	
	
}
