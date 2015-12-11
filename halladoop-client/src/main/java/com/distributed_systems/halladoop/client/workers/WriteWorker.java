package com.distributed_systems.halladoop.client.workers;

import com.distributed_systems.halladoop.client.data.Operation;
import com.distributed_systems.halladoop.client.data.WriteData;

import com.distributed_systems.halladoop.client.data.WriteException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

import static com.distributed_systems.halladoop.client.utils.FileUtils.createBlocks;

/**
 * Created by devin on 12/8/15.
 */
public class WriteWorker implements Runnable {
    private static final String WRITE = "/write";
    private static final int PORT = 4567;

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
        HttpPost writePayload = new HttpPost(WRITE);
        writePayload.addHeader("Content-Type", "application/json");

        try {
            List<WriteData> blocks = createBlocks(file);

            StringEntity entity = new StringEntity("{\"path\": \""
                    + file.getName() + "\" \"numBlocks\": "
                    + blocks.size() + "}");

            writePayload.setEntity(entity);
            CloseableHttpResponse response = client.execute(writePayload);
            int responseCode = response.getStatusLine().getStatusCode();

            if (responseCode == 200) {
                String[] dataNodes = mapper.readValue(response.getEntity().getContent(), String[].class);

                for (String ip : dataNodes) {
                    Socket connection = new Socket(ip, PORT);
                    ObjectOutputStream outputStream = new ObjectOutputStream(connection.getOutputStream());
                    ObjectInputStream inputStream = new ObjectInputStream(connection.getInputStream());

                    boolean dataNodeFinalize = true;
                    Iterator<WriteData> blockIterator = blocks.iterator();

                    while (blockIterator.hasNext() && dataNodeFinalize) {
                        WriteData block = blockIterator.next();

                        outputStream.writeObject(Operation.WRITE);
                        outputStream.writeObject(block);
                        outputStream.flush();

                        try {
                            dataNodeFinalize = (Boolean) inputStream.readObject();
                        } catch (ClassNotFoundException e) {
                            dataNodeFinalize = false;
                        }
                    }
                }
            } else {
                throw new WriteException("Server responded with: " + response);
            }
        } catch (IOException e) {
            throw new WriteException("The file " + file.getName()
                    + " was not written due to: \n" + e.getMessage());
        }
    }
}
