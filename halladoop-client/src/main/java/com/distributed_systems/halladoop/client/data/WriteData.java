package com.distributed_systems.halladoop.client.data;

import java.io.Serializable;

/**
 * Created by devin on 12/10/15.
 */
public class WriteData implements Serializable {
    private static final long serialVersionUID = 42L;

    private byte[] data;
    private String blockId;

    public WriteData(byte[] data, String blockId) {
        this.data = data;
        this.blockId = blockId;
    }

    public byte[] getData() {
        return data;
    }
}
