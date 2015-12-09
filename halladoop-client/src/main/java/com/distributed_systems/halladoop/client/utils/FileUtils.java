package com.distributed_systems.halladoop.client.utils;

import com.distributed_systems.halladoop.client.data.Block;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by devin on 12/8/15.
 */
public class FileUtils {
    public static List<Block> createBlocks(File file) throws IOException {
        List<Block> blocks = new ArrayList<>();

        try (FileInputStream inputStream = new FileInputStream(file)) {
            FileChannel channel = inputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(Block.BLOCK_SIZE);

            byte[] blockData = new byte[Block.BLOCK_SIZE];

            while (channel.read(buffer) > 0) {
                buffer.flip();

                for (int i = 0; i < buffer.limit(); i++)
                {
                    blockData[i] = buffer.get();
                }

                buffer.clear();

                Block dataBlock = new Block(blockData);
                blocks.add(dataBlock);
            }
        }

        return blocks;
    }
}
