package com.distributed_systems.halladoop.chunk_helper.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ChunkIterator implements Iterator<ChunkData> {
	private ByteArrayInputStream bais;
	private final int FOUR_KILOBYTES = 1024 * 4;
	private int currentChunk = 0;

	public ChunkIterator(Block block) {
		bais = new ByteArrayInputStream(block.getData());
	}

	public boolean hasNext() {
		return bais.available() > 0;
	}

	public ChunkData next() {
		ChunkData chunk = new ChunkData();
		byte[] data = new byte[bais.available() > FOUR_KILOBYTES ? FOUR_KILOBYTES : bais.available()];
		try {
			bais.read(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chunk.setData(data);
		chunk.setChunkNumber(currentChunk);
		currentChunk++;
		return chunk;
	}

}
