package com.distributed_systems.halladoop.client.data.name_node;

/**
 * Created by devin on 12/13/15.
 */
public class Node {
    private int node_id;
    private String node_ip;

    public int getNode_id() {
        return node_id;
    }

    public String getNode_ip() {
        return node_ip;
    }

    public void setNode_id(int node_id) {
        this.node_id = node_id;
    }

    public void setNode_ip(String node_ip) {
        this.node_ip = node_ip;
    }
}
