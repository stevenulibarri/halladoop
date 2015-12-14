package com.distributed_systems.halladoop.dataNode.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;

import com.distributed_systems.halladoop.client.data.manifest.Manifest;
import com.distributed_systems.halladoop.client.data.manifest.ManifestInfo;

/**
 * Created by devin on 12/9/15.
 */
public class ReadWorker implements Callable<File> {
    private static String READ = "/read/";
    private static final int DATA_NODE_PORT = 4567;

    private final String fileName;
    private final String host;
    private final int port;

    public ReadWorker(String fileName, String host, int port) {
        this.host = host;
        this.port = port;
        this.fileName = fileName;
    }

    @Override
    public File call() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CloseableHttpClient client = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder();

        try {
            URI uri = uriBuilder.setHost(host).setPort(port)
                    .setPath(READ + fileName).build();

            HttpGet get = new HttpGet(uri);
            HttpResponse response = client.execute(get);

            if (response.getStatusLine().getStatusCode() != 500) {

                FileOutputStream output = new FileOutputStream(fileName, true);
                Manifest manifest = mapper.readValue(response.getEntity().getContent(), Manifest.class);
                ManifestInfo[] manifestInfo = manifest.getManifest();

                for (ManifestInfo info : manifestInfo) {
                    int current = 0;
                    boolean nodeDown = true;
                    String nodes[] = info.getNodes();

                    while (current < nodes.length && nodeDown) {
                        try (Socket connection = new Socket(nodes[current], DATA_NODE_PORT)) {
                            ObjectOutputStream outputStream = new ObjectOutputStream(connection.getOutputStream());
                            ObjectInputStream inputStream = new ObjectInputStream(connection.getInputStream());

                            outputStream.writeObject(Operation.READ);
                            outputStream.flush();

                            byte[] block = (byte[]) inputStream.readObject();
                            output.write(block);

                            nodeDown = false;
                        } catch (IOException ex) {
                            nodeDown = true;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                throw new Exception("The server returned with a 500");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(fileName);
    }
}
