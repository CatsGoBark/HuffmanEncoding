import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created by fanzeyi on 5/8/16.
 */
public class HuffmanEncoder {
    private static final int BUFFER_SIZE = 512;
    private String filename;
    private HashMap<Byte, Integer> countMap;

    public HuffmanEncoder(String filename) {
        this.filename = filename;
        this.countMap = new HashMap<>();
    }

    public void calculateQuantity() {
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(this.filename));
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;

            while ((length = dis.read(buffer)) != -1) {
                for (int i=0; i < length; i++) {
                    countMap.put(buffer[i], countMap.getOrDefault(buffer[i], 0) + 1);
                }
            }

            dis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

//    private HuffNode buildHuffmanTree() {
//        PriorityQueue<HuffNode> queue = new PriorityQueue<>();
//
//        countMap.forEach((k, v) -> {
//            HuffNode n = new HuffNode(k, v);
//            queue.add(n);
//        });
//
//        HuffNode root = null;
//        while (queue.size() > 1)  {
//            HuffNode left = queue.remove();
//            HuffNode right = queue.remove();
//
//            HuffNode total = new HuffNode(left.frequency + right.frequency);
//
//            total.right = left;
//            total.left = right;
//
//            left.parent = total;
//            right.parent = total;
//            queue.add(total);
//            // go until the root is found
//            root = total;
//        }
//        return root;
//    }
}
