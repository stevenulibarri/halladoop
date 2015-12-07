package com.distributed_systems.halladoop.dataNode;

import spark.Spark;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	DataNodeApi api = new DataNodeApi();
        Spark.get("/read", (request, response) -> api.readFile(request, response));
        
		Spark.post("/write", (request, response) -> api.writeFile(request, response));
    }
    
    public static void processBytes(byte[] bytes){
    	
    }
    // /packet(){http request() -> block ID, Raw Data, PipelineNode, Packet #/Total}
}
