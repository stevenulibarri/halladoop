package com.distributed_systems.halladoop.client.workers;

import com.distributed_systems.halladoop.client.data.Block;

import java.util.concurrent.Callable;

/**
 * Created by devin on 12/9/15.
 */
public class ReadWorker implements Runnable, Callable<Block> {
    @Override
    public Block call() throws Exception {
        return null;
    }

    @Override
    public void run() {

    }
}
