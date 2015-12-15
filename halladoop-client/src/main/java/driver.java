import com.distributed_systems.halladoop.client.HalladoopClient;

import java.io.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by devin on 12/9/15.
 */
public class driver {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\test\\kernel7.img");

        HalladoopClient client = new HalladoopClient("104.236.162.28", 8080);
        //client.write("test/kernel7.img", file);
        
        try {
			client.read("test/kernel7.img", "C:\\test\\testKernel.bin");
		} catch (ExecutionException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
