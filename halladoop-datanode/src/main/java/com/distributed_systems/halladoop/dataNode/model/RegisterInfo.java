package com.distributed_systems.halladoop.dataNode.model;

public class RegisterInfo {

	private String nodeIP;
	private long totalDiskSpace;
	private long availableDiskSpace;
	
	public RegisterInfo(String nodeIp, long totalDisk, long availableDisk){
		this.nodeIP = nodeIp;
		this.totalDiskSpace = totalDisk;
		this.availableDiskSpace = availableDisk;
	}
	
	public String getNodeIP() {
		return nodeIP;
	}
	public void setNodeIP(String nodeIP) {
		this.nodeIP = nodeIP;
	}
	public long getTotalDiskSpace() {
		return totalDiskSpace;
	}
	public void setTotalDiskSpace(long totalDiskSpace) {
		this.totalDiskSpace = totalDiskSpace;
	}
	public long getAvailableDiskSpace() {
		return availableDiskSpace;
	}
	public void setAvailableDiskSpace(long availableDiskSpace) {
		this.availableDiskSpace = availableDiskSpace;
	}
	
	
}
