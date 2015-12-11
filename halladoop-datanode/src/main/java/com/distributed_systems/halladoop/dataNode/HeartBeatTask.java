package com.distributed_systems.halladoop.dataNode;

import java.io.File;
import java.io.IOException;
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

public class HeartBeatTask extends TimerTask{

	private final int PORT = 4567;
	
	@Override
	public void run(){
		HttpClient client = HttpClients.createDefault();
		HeartbeatInfo info = new HeartbeatInfo();
		File file = new File(App.getCorePath());
		info.setAvailableDiskSpace(file.getUsableSpace());
		info.setBlockManifest(App.getFiles().keySet());
		info.setNodeID(App.getId());
		HttpPost post = new HttpPost();
		try {
			ObjectMapper mapper = new ObjectMapper();
			String a = mapper.writeValueAsString(info);
			HttpEntity entity = new StringEntity(a);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
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
