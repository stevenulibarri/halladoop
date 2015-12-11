package com.distributed_systems.halladoop.client.workers;

import com.distributed_systems.halladoop.client.data.Block;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;

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
    private final String host;

    public WriteWorker(File file, String host) {
        this.file = file;
        this.host = host;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost writePayload = new HttpPost();
        writePayload.addHeader("Content-Type", "application/json");

        try {
            List<Block> blocks = createBlocks(file);

            StringEntity entity = new StringEntity("\"path\": \""
                    + file.getName() + "\" \"numBlocks\": "
                    + blocks.size() + "");

            writePayload.setEntity(entity);
            CloseableHttpResponse response = client.execute(writePayload);
            String[] dataNodes = mapper.readValue(response.getEntity().getContent(), String[].class);

            // TODO: Add async sockets to prevent from spinning off x more child processes
            for (Block block : blocks) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
