package com.distributed_systems.halladoop.client.data;

/**
 * Created by devin on 12/8/15.
 */
public class Block {
    public static final int BLOCK_SIZE = 64 * 1048576;

    private final byte[] blockData;

    public Block(byte[] blockData) {
        this.blockData = blockData;
    }
}
