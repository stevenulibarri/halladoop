package com.distributed_systems.halladoop.client.workers;

import com.distributed_systems.halladoop.client.data.Block;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.distributed_systems.halladoop.client.utils.FileUtils.createBlocks;

/**
 * Created by devin on 12/8/15.
 */
public class WriteWorker implements Runnable {
    private static final String WRITE_PIPELINE = "/getWritePipeline";
    private static final String READ_MANIFEST = "/getReadManifest";

    private final File file;

    public WriteWorker(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        List<Block> blocks = null;
        try {
            blocks = createBlocks(file);

            for (Block block : blocks) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
