package com.distributed_systems.halladoop.dataNode;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;

import com.distributed_systems.halladoop.dataNode.model.RegisterInfo;


/**
 * Hello world!
 *
 */
public class App 
{
	private static ObjectMapper mapper = new ObjectMapper();
	public final String nameNode = "";
	private static Map<String, String> files = new HashMap<String, String>();
	private static String corePath;
	private static String nodeID;
	private static final int FILES_PER_DIRECTORY = 20;
	private static int filesInDirectory = 0;
	private static long directoryNumber;
	private static Map<String, String> directoryMap = new HashMap<String, String>();

	
    public static void main( String[] args )
    {
    	corePath = "/"; //Get from command arg
    	String ip = "127.0.0.1"; //Also get from command args to circumvent complicated ip issues
		try(ServerSocket server = new ServerSocket(4568)){
			HttpClient client = HttpClients.createDefault();
			File everything = new File(corePath);
			RegisterInfo registerInfo = new RegisterInfo(ip, everything.getTotalSpace(), everything.getUsableSpace());
			HttpPost post = new HttpPost();
			HttpEntity entity = new StringEntity(mapper.writeValueAsString(registerInfo));
			post.setEntity(entity);
			client.execute(post);
			while(true){
				Socket socket = server.accept();
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
    
    public static String getId(){
    	return nodeID;
    }
    
    public static void deleteFile(String blockId){
    	
    }
    
    public static boolean replicateFile(String blockId, String nodeIp, int nodePort){
    	try {
			Socket socket = new Socket(nodeIp, nodePort);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
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
