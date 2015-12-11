package com.distributed_systems.halladoop.client.workers;

import com.distributed_systems.halladoop.client.data.WriteData;

import java.util.concurrent.Callable;

/**
 * Created by devin on 12/9/15.
 */
public class ReadWorker implements Runnable, Callable<WriteData> {
    private static String READ = "/read";

    private final String fileName;
    private final String host;
    private final int port;

    public ReadWorker(String fileName, String host, int port) {
        this.host = host;
        this.port = port;
        this.fileName = fileName;
    }

    @Override
    public WriteData call() throws Exception {
        return null;
    }

    @Override
    public void run() {

    }
}
