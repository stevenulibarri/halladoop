package com.distributed_systems.halladoop.client.data.manifest;

/**
 * Created by devin on 12/13/15.
 */
public class ManifestInfo {
    private String block_id;
    private String[] nodes;

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    public String[] getNodes() {
        return nodes;
    }

    public void setNodes(String[] nodes) {
        this.nodes = nodes;
    }
}
