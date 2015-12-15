package com.distributed_systems.halladoop.dataNode.model;

import java.util.List;

public class FinalizeInfo {

	private String block_id;
	private List<Integer> nodes;
	
	public FinalizeInfo(String block_id, List<Integer> nodes){
		this.block_id = block_id;
		this.nodes = nodes;
	}
	
	public String getBlock_id() {
		return block_id;
	}
	public void setBlock_id(String block_id) {
		this.block_id = block_id;
	}
	public List<Integer> getNodes() {
		return nodes;
	}
	public void setNodes(List<Integer> nodes) {
		this.nodes = nodes;
	}
	
	
}
