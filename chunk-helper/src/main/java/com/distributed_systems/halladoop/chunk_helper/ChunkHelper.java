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
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import spark.Request;

import com.distributed_systems.halladoop.chunk_helper.model.Block;
import com.distributed_systems.halladoop.chunk_helper.model.ChunkData;
import com.distributed_systems.halladoop.chunk_helper.model.ChunkIterator;

import spark.Request;

/**
 * Hello world!
 *
 */
public class ChunkHelper {
	private int fileSize;
	private Block block;

	public ChunkHelper(int fileSize) {
		this.fileSize = fileSize;
		block = new Block(fileSize);
	}

	public ChunkHelper(int fileSize, byte[] data){
		this(fileSize);
		block.setData(data);
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

	public ChunkData processChunk(Request request) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		ChunkData data = mapper.readValue(request.bodyAsBytes(), ChunkData.class);
		return data;
	}
	// addChunk(ChunkData)
	public void addChunk(ChunkData chunk){
		block.addChunk(chunk);
	}
	
	public byte[] getData(){
		if(block.getData() != null && block.getData().length != 0){
			return block.getData();
		}
		throw new RuntimeException("No data in the block. Incomplete transfer.");
	}
}
