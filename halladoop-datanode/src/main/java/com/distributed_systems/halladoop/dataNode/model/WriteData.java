package com.distributed_systems.halladoop.dataNode.model;

import java.io.Serializable;

public class WriteData implements Serializable {
	private static final long serialVersionUID = 42L;

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
