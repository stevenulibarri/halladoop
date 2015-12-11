package com.distributed_systems.halladoop.dataNode.model;

public class WriteData {
	private byte[] data;
	private String blockId;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}
}
