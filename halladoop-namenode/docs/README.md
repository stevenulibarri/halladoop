# halladoop-namenode

Maintains virtual filesystem image and coordinates io and replication for the entire cluster.

### Endpoints
##### POST /register
called by a newly started datanode to inform the namenode of its existence
###### Request:
````javascript
{
  "nodeId": "id",
  "nodeIP": "1.12.13.1",
  "totalDiskSpaceMB": 1337,
  "availableDiskSpace": 1337
}
````

##### POST /getWritePipeline
called by client to obtain a list of datanodes to recieve blacks for a write
###### Request:
````javascript
{
  "clientIP": "1.1.1.1",
  "filePath": "some/file/path/lol.txt",
  "fileSizeMB": 1234,
}
````

###### Response:
````javascript
{
  "dataNodes": [
    { "nodeIP": "1.1.1.1" },
    { "nodeIP": "1.1.1.2" },
    { "nodeIP": "1.1.1.3" }
  ]
}
````

##### POST /getReadManifest
called by clients to obtain a manifest of blocks and their locations on datanodes for a file
##### Request:
````javascript
{
  "filePath": "some/path/file.txt",
}
````
##### Response:
````javascript
{
  "fileBlockManifest": {
    //describes all blocks for a file and the datanodes that own them
    //includes unique identifier of each block on each node
  }
}
````

##### POST /heartbeat
called by datanodes to communicate datanode status to the namenode
###### Request:
````javascript
{
  "nodeId": "1.1.1.1",
  "availableDiskSpace": 1234,
  "blockManifest": {
    //describes all blocks currently stored on this datanode
  }
}
````
###### Response:
````javascript
{
  "actions": [
    //list of objects that represent actions that need to be taken by this datanode
    //may include requesting block replication from another data node or deleting blocks
  ]
}
````
