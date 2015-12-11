package com.distributed_systems.halladoop.dataNode.model;

public class RegisterInfo {

	private String node_ip;
	private long total_disk_space_mb;
	private long available_disk_space_mb;
	
	public RegisterInfo(){
		
	}
	
	public RegisterInfo(String nodeIp, long totalDisk, long availableDisk){
		this.node_ip = nodeIp;
		this.total_disk_space_mb = totalDisk;
		this.available_disk_space_mb = availableDisk;
	}


	public long getTotal_disk_space_mb() {
		return total_disk_space_mb;
	}

	public void setTotal_disk_space_mb(long total_disk_space_mb) {
		this.total_disk_space_mb = total_disk_space_mb;
	}

	public long getAvailable_disk_space_mb() {
		return available_disk_space_mb;
	}

	public void setAvailable_disk_space_mb(long available_disk_space_mb) {
		this.available_disk_space_mb = available_disk_space_mb;
	}

	public String getNode_ip() {
		return node_ip;
	}

	public void setNode_ip(String node_ip) {
		this.node_ip = node_ip;
	}

	
	
}
