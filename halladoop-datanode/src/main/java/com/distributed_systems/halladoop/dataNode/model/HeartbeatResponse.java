package com.distributed_systems.halladoop.dataNode.model;

public class HeartbeatResponse {

	private String[] delete;
	private ReplicationResponse[] replication;
	
	public String[] getDelete() {
		return delete;
	}
	public void setDelete(String[] delete) {
		this.delete = delete;
	}
	public ReplicationResponse[] getReplication() {
		return replication;
	}
	public void setReplication(ReplicationResponse[] replication) {
		this.replication = replication;
	}
	
	
}
