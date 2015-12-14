package com.distributed_systems.halladoop.client.workers;

import com.distributed_systems.halladoop.client.data.WriteData;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;

/**
 * Created by devin on 12/9/15.
 */
public class ReadWorker implements Runnable, Callable<WriteData> {
    private static String READ = "/read/";

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
        ObjectMapper mapper = new ObjectMapper();
        CloseableHttpClient client = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder();

        try {
            URI uri = uriBuilder.setHost(host).setPort(port)
                    .setPath(READ + fileName).build();

            HttpGet get = new HttpGet(uri);
            HttpResponse response =  client.execute(get);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
