package com.distributed_systems.halladoop.client;

import com.distributed_systems.halladoop.client.workers.WriteWorker;

import java.io.File;

import java.io.InputStream;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by devin on 12/3/15.
 */
public class HalladoopClient {
    public static final int BLOCK_SIZE = 64 * 1048576;
    private static final int cores = Runtime.getRuntime().availableProcessors();

    private final String NAME_NODE_ADDRESS;
    private final int PORT;
    private final ExecutorService workers;

    public HalladoopClient(String nameNodeAddress, int port) {
        this.PORT = port;
        this.NAME_NODE_ADDRESS = nameNodeAddress;
        this.workers = Executors.newFixedThreadPool(cores);
    }

    public void write(File file) {
        WriteWorker writeWorker = new WriteWorker(file, NAME_NODE_ADDRESS, PORT);
        workers.execute(writeWorker);
    }

    public InputStream read(String fileName) {
        return null;
    }
}