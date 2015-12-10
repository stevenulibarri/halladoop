import com.distributed_systems.halladoop.client.data.Block;
import com.distributed_systems.halladoop.client.utils.FileUtils;

import java.io.*;
import java.util.List;

/**
 * Created by devin on 12/9/15.
 */
public class driver {
    public static void main(String[] args) throws IOException {
        File file = new File("/home/devin/gentoo_root.img");
        List<Block> blocks = FileUtils.createBlocks(file);

        System.out.println(blocks.size());
    }
}
