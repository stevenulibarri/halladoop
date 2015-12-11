import com.distributed_systems.halladoop.client.HalladoopClient;

import java.io.*;

/**
 * Created by devin on 12/9/15.
 */
public class driver {
    public static void main(String[] args) throws IOException {
        File file = new File("/home/devin/gentoo_root.img");

        HalladoopClient client = new HalladoopClient("104.236.162.28", 8080);
        client.write(file);
    }
}
