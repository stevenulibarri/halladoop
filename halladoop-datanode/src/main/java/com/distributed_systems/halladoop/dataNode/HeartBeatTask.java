package com.distributed_systems.halladoop.dataNode;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;

import com.distributed_systems.halladoop.dataNode.model.HeartbeatInfo;
import com.distributed_systems.halladoop.dataNode.model.HeartbeatResponse;
import com.distributed_systems.halladoop.dataNode.model.ReplicationResponse;

import spark.utils.IOUtils;

public class HeartBeatTask extends TimerTask{

	private final int PORT = 4567;
	private final String url = "http://104.236.162.28:8080/heartbeat/";
	
	@Override
	public void run(){
		HttpClient client = HttpClients.createDefault();
		HeartbeatInfo info = new HeartbeatInfo();
		File file = new File(App.getCorePath());
		info.setavailable_disk_space_mb(file.getUsableSpace());
		info.setblock_manifest(App.getFiles().keySet());
		info.setnode_id(App.getId());
		HttpPost post = new HttpPost(url);
		try {
			ObjectMapper mapper = new ObjectMapper();
			HttpEntity entity = new StringEntity(mapper.writeValueAsString(info));
			System.out.println(mapper.writeValueAsString(info));
			post.setEntity(entity);
			post.setHeader("Content-Type", "application/json");
			HttpResponse response = client.execute(post);
			StringWriter writer = new StringWriter();
			IOUtils.copy(response.getEntity().getContent(), writer);
			String theString = writer.toString();
			System.out.println(theString);
			System.out.println(response.getEntity().getContent());
			HeartbeatResponse heartbeat =  mapper.readValue(response.getEntity().getContent(), HeartbeatResponse.class);
			for(String delete : heartbeat.getDelete()){
				if (App.getFiles().containsKey(delete)) {
					File deleteFile = new File(App.getFiles().get(delete));
					deleteFile.delete();
				}
			}
			replication: for(ReplicationResponse r : heartbeat.getReplication()){
				for(String node : r.getNodes()){
					if(App.replicateFile(r.getBlockID(), node, PORT)){
						break replication;
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
