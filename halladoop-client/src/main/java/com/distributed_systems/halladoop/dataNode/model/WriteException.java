package com.distributed_systems.halladoop.dataNode.model;

/**
 * Created by devin on 12/10/15.
 */
public class WriteException extends RuntimeException {
    public WriteException(String message) {
        super(message);
    }
}
