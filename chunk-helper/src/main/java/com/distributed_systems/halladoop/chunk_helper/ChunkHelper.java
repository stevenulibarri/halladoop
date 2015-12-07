package com.distributed_systems.halladoop.chunk_helper;

import java.io.IOException;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.distributed_systems.halladoop.chunk_helper.model.Block;
import com.distributed_systems.halladoop.chunk_helper.model.ChunkData;
import com.distributed_systems.halladoop.chunk_helper.model.ChunkIterator;

/**
 * Hello world!
 *
 */
public class ChunkHelper {
	private int fileSize;
	private Block block;
	private final int FOUR_KILOBYTES = 1024 * 4;

	public ChunkHelper(int fileSize) {
		this.fileSize = fileSize;
		int totalChunks = fileSize / FOUR_KILOBYTES;
		if (fileSize % FOUR_KILOBYTES != 0) {
			totalChunks++;
		}
		block = new Block(totalChunks);
	}

	 public Iterator<ChunkData> getChunks(Block block) {
		 return new ChunkIterator(block);
	 }

	public CloseableHttpResponse sendChunk(String endpoint, ChunkData chunk) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(endpoint);
		ObjectMapper mapper = new ObjectMapper();
		CloseableHttpResponse response = null;
		try {
			String chunkString = mapper.writeValueAsString(chunk);
			HttpEntity entity = new StringEntity(chunkString);
			postRequest.setEntity(entity);
			response = client.execute(postRequest);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

//	public ChunkData processChunk(Request request){
//		
//	}
	// addChunk(ChunkData)
}
