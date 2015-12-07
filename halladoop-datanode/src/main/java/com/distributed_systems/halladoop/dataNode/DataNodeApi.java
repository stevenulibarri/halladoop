package com.distributed_systems.halladoop.dataNode;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.distributed_systems.halladoop.dataNode.model.PostData;
import com.distributed_systems.halladoop.dataNode.model.ReadData;

import spark.Request;
import spark.Response;

public class DataNodeApi {
	private final int FILES_PER_DIRECTORY = 20;
	private int filesInDirectory = 0;
	private long directoryNumber;
	private Map<String, String> directoryMap = new HashMap<String, String>();

	public void api() {

	}

	public Response writeFile(Request request, Response response)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
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
		ObjectMapper mapper = new ObjectMapper();
		ReadData data = mapper.readValue(request.bodyAsBytes(), ReadData.class);
		File fileToRead = new File(directoryMap.get(data.getBlockId()));
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToRead));
		byte[] file = new byte[(int) fileToRead.length()];
		bis.read(file);
		response.
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
