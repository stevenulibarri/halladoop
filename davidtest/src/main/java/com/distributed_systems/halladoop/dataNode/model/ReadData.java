package com.distributed_systems.halladoop.dataNode.model;

import java.io.Serializable;

public class ReadData  implements Serializable{
	private String blockId;

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

}
