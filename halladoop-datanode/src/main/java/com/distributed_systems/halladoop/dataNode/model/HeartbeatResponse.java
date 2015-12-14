package com.distributed_systems.halladoop.dataNode.model;

public class HeartbeatResponse {

	private String[] delete;
	private ReplicationResponse[] replicate;
	
	public String[] getDelete() {
		return delete;
	}
	public void setDelete(String[] delete) {
		this.delete = delete;
	}
	public ReplicationResponse[] getReplicate() {
		return replicate;
	}
	public void setReplicate(ReplicationResponse[] replicate) {
		this.replicate = replicate;
	}
	
	
}
