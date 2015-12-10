package com.distributed_systems.halladoop.dataNode;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;

import com.distributed_systems.halladoop.dataNode.model.HeartbeatInfo;
import com.distributed_systems.halladoop.dataNode.model.HeartbeatResponse;
import com.distributed_systems.halladoop.dataNode.model.ReplicationResponse;

public class HeartBeatTask extends TimerTask{

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
			HttpResponse response = client.execute(post);
			ObjectMapper mapper = new ObjectMapper();
			HeartbeatResponse heartbeat =  mapper.readValue(response.getEntity().getContent(), HeartbeatResponse.class);
			replication: for(ReplicationResponse r : heartbeat.getReplication()){
				for(String node : r.getNodes()){
					if(App.replicateFile(r.getBlockID(), node)){
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
