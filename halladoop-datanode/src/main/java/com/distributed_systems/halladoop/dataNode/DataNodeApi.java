package com.distributed_systems.halladoop.dataNode;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import spark.Request;
import spark.Response;

import com.distributed_systems.halladoop.chunk_helper.ChunkHelper;
import com.distributed_systems.halladoop.chunk_helper.model.Block;
import com.distributed_systems.halladoop.chunk_helper.model.ChunkData;
import com.distributed_systems.halladoop.dataNode.model.InitializeData;
import com.distributed_systems.halladoop.dataNode.model.PostData;
import com.distributed_systems.halladoop.dataNode.model.ReadData;

public class DataNodeApi {
	private static ObjectMapper mapper = new ObjectMapper();
	private final int FILES_PER_DIRECTORY = 20;
	private int filesInDirectory = 0;
	private long directoryNumber;
	private Map<String, String> directoryMap = new HashMap<String, String>();
	private Map<String, Iterator<ChunkData>> dataToSend = new HashMap<String, Iterator<ChunkData>>();
	private ChunkHelper writer;
	
	public void api() {

	}

	public Response initializeResponse(Request request, Response response){
		try {
			InitializeData data = mapper.readValue(request.body(), InitializeData.class);
			writer = new ChunkHelper(data.getFileSize());
			response.status(202);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public Response writeFile(Request request, Response response)
			throws JsonParseException, JsonMappingException, IOException {
		PostData data = mapper.readValue(request.bodyAsBytes(), PostData.class);
		File directory = new File(getNextPath(data.getBlockId()));
		if (!directory.exists()) {
			directory.mkdir();
		}
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(directory.getPath() + "/" + data.getBlockId() + ".bin"));
		bos.write(data.getData());
		bos.flush();
		bos.close();
		response.status(201);
		System.out.println("Wrote the big file.");
		return response;
	}

	
	
	public Response readFile(Request request, Response response)
			throws JsonParseException, JsonMappingException, IOException {
		ReadData data = mapper.readValue(request.bodyAsBytes(), ReadData.class);
		//File fileToRead = new File(directoryMap.get(data.getBlockId()));
		File fileToRead = new File("C:\\test\\file-1.bin");
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToRead));
		byte[] file = new byte[(int) fileToRead.length()];
		bis.read(file);
		bis.close();
		ChunkHelper helper = new ChunkHelper((int) fileToRead.length(), file);
		Block b = new Block((int)fileToRead.length(), file);
		Iterator<ChunkData> iterator = helper.getChunks(b);

		try {
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost(data.getEndpoint());
			while(iterator.hasNext()){
				ChunkData cd = iterator.next();
				String information = mapper.writeValueAsString(cd);
				HttpEntity entity = new StringEntity(information);
				post.setEntity(entity);
				CloseableHttpResponse httpResponse = client.execute(post);
				httpResponse.close();
			}
			client.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	private String getNextPath(String blockId) {
		if (filesInDirectory >= FILES_PER_DIRECTORY) {
			directoryNumber++;
			filesInDirectory = 0;
		}
		filesInDirectory++;
		String path = "/home/kevin/" + directoryNumber + "/";
		directoryMap.put(blockId, path + "/" + blockId + ".bin");
		return path;
	}
}
