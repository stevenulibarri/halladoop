# halladoop
A distrubted file system based on hadoop.

Consists of three components
- [namenode](halladoop-namenode/docs/README.md)
  - maintains image of the virtual file system across the cluster
  - ensures file blocks are replicated across 3 datanodes to provide fault-tolerance
- [datanode](halladoop-datanode/docs/README.md)
  - stores/replicates/streams file blocks
- [clients](halladoop-client/docs/README.md)
  - read/write files to the cluster

### Process Diagrams
#### Writes
1. Client requests list of available datanodes from namenode, these nodes form a pipeline
2. Client chunks file into blocks
3. Each block is streamed to the datanodes in individual packets
4. Packets are propagated through datanodes
5. Last datanode sends confirmation of each packet to client
6. When all packets are confirmed client informs namenode that the block in finalized
7. Namenode writes the block to the virtual filesytem image
8. Repeat until all file blocks are finalized
![write diagram](http://files.stevenulibarri.com/halladoop_docs/write.png)

If a datanode failes the client removes the node from the pipeline and replays lost packets to the remaining nodes. The name node will eventually ensure that the blocks are replicated elsewhere.

#### Reads
1. Client requests block manifest for file from namenode which contains a map of each block to the datanodes that contain it
2. For each block the client requests it from one of the nodes
3. The datanode streams the block back to the client in packets
4. Repeat until all blocks are obtained.
![read diagram](http://files.stevenulibarri.com/halladoop_docs/read.png)

If a datanode fails the client attempts to obtain the block from another datanode in the manifest mapping.
