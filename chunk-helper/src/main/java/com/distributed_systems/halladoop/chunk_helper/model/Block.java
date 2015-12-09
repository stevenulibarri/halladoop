package com.distributed_systems.halladoop.chunk_helper.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Block {
	private int nextChunk;
	private int totalChunks;
	private ByteArrayOutputStream baos;
	private byte[] data;
	private final int FOUR_KILOBYTES = 1024 * 4;


	public Block(int fileSize){
		int totalChunks = fileSize / FOUR_KILOBYTES;
		if (fileSize % FOUR_KILOBYTES != 0) {
			totalChunks++;
		}
		this.totalChunks = totalChunks;
		this.nextChunk = 0;
		baos = new ByteArrayOutputStream();
	}

	public Block(int fileSize, byte[] data){

		this.data = data;
	}
	
	public void addChunk(ChunkData chunk) {
		if (chunk.getChunkNumber() == nextChunk && chunk.getChunkNumber() < totalChunks) {
			try {
				baos.write(chunk.getData());
				if (chunk.getChunkNumber() == totalChunks) {
					data = baos.toByteArray();
					baos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		nextChunk++;
	}

	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data){
		this.data = data;
	}
}
