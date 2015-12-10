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
    private static final int cores = Runtime.getRuntime().availableProcessors();

    private final String NAME_NODE_ADDRESS;
    private final ExecutorService workers;

    private HalladoopClient(String NAME_NODE_ADDRESS) {
        this.NAME_NODE_ADDRESS = NAME_NODE_ADDRESS;
        this.workers = Executors.newFixedThreadPool(cores);
    }

    public void write(File file) {
        WriteWorker writeWorker = new WriteWorker(file);
        workers.execute(writeWorker);
    }

    public InputStream read(String fileName) {
        return null;
    }
}