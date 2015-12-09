package com.distributed_systems.halladoop.chunk_helper.model;

public class ChunkData {
	private byte[] data;
	private long chunkNumber;

	public long getChunkNumber() {
		return chunkNumber;
	}

	public void setChunkNumber(long chunkNumber) {
		this.chunkNumber = chunkNumber;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
