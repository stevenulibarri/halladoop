package com.distributed_systems.halladoop.dataNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;

import com.distributed_systems.halladoop.dataNode.model.FinalizeInfo;
import com.distributed_systems.halladoop.dataNode.model.Operation;
import com.distributed_systems.halladoop.dataNode.model.ReadData;
import com.distributed_systems.halladoop.dataNode.model.RegisterInfo;
import com.distributed_systems.halladoop.dataNode.model.RegisterResponse;
import com.distributed_systems.halladoop.dataNode.model.WriteData;
import com.distributed_systems.halladoop.io.BlockReader;


/**
 * Hello world!
 *
 */
public class App 
{
	private static ObjectMapper mapper = new ObjectMapper();
	public final String nameNode = "";
	private static String endpoint = "http://104.236.162.28:8080/";
	private static Map<String, String> files = new HashMap<String, String>();
	private static String corePath;
	private static int nodeID;
	private static final int FILES_PER_DIRECTORY = 20;
	private static int filesInDirectory = 0;
	private static long directoryNumber;
	private static Map<String, String> directoryMap = new HashMap<String, String>();

	
    public static void main( String[] args )
    {
    	corePath = args[0]; //Get from command arg
    	String ip = args[1]; //Also get from command args to circumvent complicated ip issues
		try(ServerSocket server = new ServerSocket(4567)){
			HttpClient client = HttpClients.createDefault();
			File everything = new File(corePath);
			RegisterInfo registerInfo = new RegisterInfo();
			registerInfo.setNode_ip(ip);
			registerInfo.setAvailable_disk_space_mb(everything.getUsableSpace() / 1024 / 1024);
			registerInfo.setTotal_disk_space_mb(everything.getTotalSpace() / 1024 / 1024);
			HttpPost post = new HttpPost(endpoint + "register/");
			HttpEntity entity = new StringEntity("{\"node_ip\":\""+ registerInfo.getNode_ip() + "\",\"total_disk_space_mb\":\""+registerInfo.getTotal_disk_space_mb()+"\",\"available_disk_space_mb\":\"" + registerInfo.getAvailable_disk_space_mb() +"\"} ");
			System.out.println(entity);
			post.setEntity(entity);
			post.setHeader("Content-Type", "application/json");
			HttpResponse response = client.execute(post);

//			StringWriter writer = new StringWriter();
//			IOUtils.copy(response.getEntity().getContent(), writer);
//			String theString = writer.toString();
//			System.out.println(theString);
			
			RegisterResponse rr = mapper.readValue(response.getEntity().getContent(), RegisterResponse.class);
			nodeID = rr.getnode_id();
			Timer timer = new Timer();
			timer.schedule(new HeartBeatTask(), 0, 60000);
			while(true){
				Socket socket = server.accept();
				Thread t = new Thread(new BlockReader(socket));
				t.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void processBytes(byte[] bytes){
    	
    }
    
    public static Map<String, String> getFiles(){
    	return files;
    }
    
    public static String getCorePath(){
    	return corePath;
    }
    
    public static int getId(){
    	return nodeID;
    }
    
    public static void deleteFile(String blockId){
    	
    }
    
    public static boolean replicateFile(String blockId, String nodeIp, int nodePort){
    	try {
			Socket socket = new Socket(nodeIp, nodePort);
			Operation operation = Operation.READ;
			ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
			stream.writeObject(operation);
			ReadData readData = new ReadData();
			readData.setBlockId(blockId);
			stream.writeObject(readData);
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			WriteData writeData = (WriteData) inputStream.readObject();
			String path = App.getNextPath(writeData.getBlockId());
			File writeFile = new File(path);
			writeFile.mkdir();
			FileOutputStream fileOutput = new FileOutputStream(writeFile);
			fileOutput.write(writeData.getData());
			fileOutput.flush();
			fileOutput.close();
			
			HttpClient client = HttpClients.createDefault();
			FinalizeInfo finalize = new FinalizeInfo();
			finalize.setBlock_id(blockId);
			finalize.setNode_id(new int[]{nodeID});
			HttpEntity entity = new StringEntity(mapper.writeValueAsString(finalize));
			HttpPost post = new HttpPost(endpoint);
			post.setEntity(entity);
			client.execute(post);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    }
    
	public static String getNextPath(String blockId) {
		if (filesInDirectory >= FILES_PER_DIRECTORY) {
			directoryNumber++;
			filesInDirectory = 0;
		}
		filesInDirectory++;
		String path = "/home/kevin/" + directoryNumber + "/";
		directoryMap.put(blockId, path + "/" + blockId + ".bin");
		return path;
	}
    // /packet(){http request() -> block ID, Raw Data, PipelineNode, Packet #/Total}
}
