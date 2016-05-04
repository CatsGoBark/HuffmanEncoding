package cs146;

import java.io.*;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 *  Topics touched. Huffman encoding/decoding, trees, priority queues, hashmaps.
 *
 *  TODO:   Either require an instance of HuffmanCoder or make everything static for consistency
 *
 */
public class HuffmanCoder {

    public final static int BUFFER_SIZE = 512; //Allows for more efficient reading
    private String filename;
    HashMap<Byte, Integer> countMap;           //Stores the byte quantity count for a file
    HashMap<Byte, String> buildMap;            //The map used to encode and decode files.

    public HuffmanCoder(String filename) {
        this.filename = filename;
    }

    // Creates an empty file in the current directory
    // Takes in a file name including file type as a parameter.
    public static void createFile(String name) {
        try {
            File file = new File(name);
            if (file.createNewFile())
                System.out.println("File created");
            else
                System.out.println("File already exists");
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // Counts the number of kinds of Bytes in hex and stores it into countMap
    // The type of byte (i.e 25, 3d, ff, etc) is the key and the quantity is the data
    //
    // POSSIBLE ISSUE: Byte "00000000" gets stored as "0" instead of "00"
    //
    public void getQuantityHashMap() {
        try {
            File file = new File(this.filename);
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            this.countMap = new HashMap<>();

            byte[] buffer = new byte[BUFFER_SIZE];
            int length;

            while ((length = dis.read(buffer)) != -1) {
                for (int i=0; i< length; i++) {
                    countMap.put(buffer[i], countMap.getOrDefault(buffer[i], 0) + 1);
                }
            }
            dis.close();
        }
        catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // Prints contents of countMap
    public void printCountMap() {
        countMap.forEach((k, v) -> {
            System.out.printf("%x = %d\n", k, v);
        });
    }

    // Creates a huffman tree from values a hashmap.
    // TODO: Generate the encoding value and store it in HuffNode? Needs discussion
    public static HuffNode makeHuffmanTree(HashMap<Byte, Integer> hashmap) {
        PriorityQueue<HuffNode> queue = new PriorityQueue<HuffNode>();
        hashmap.forEach((k, v) -> {
            HuffNode n = new HuffNode(k, v);
            queue.add(n);
        });
        HuffNode root = null;
        while (queue.size() > 1)  {
            HuffNode left = queue.remove();
            HuffNode right = queue.remove();
            HuffNode total = new HuffNode(left.frequency + right.frequency);
            total.right = left;
            total.left = right;
            left.parent = total;
            right.parent = total;
            queue.add(total);
            // go until the root is found
            root = total;
        }
        return root;
    }

    // TODO:
    private void buildCode(HuffNode x, String s) {
        if (!x.isLeaf()) {
            buildCode(x.left, s + '0');
            buildCode(x.right, s + '1');
        }
    }

    // Uses the Huffman tree to encode a file.
    // Takes in a file and a tree and creates a new file with the extension HUFF + original extension
    // (i.e .txt -> .HUFFTXT)
    public void encode(String filename, HashMap hashmap) {
        String ext = filename.substring(filename.lastIndexOf('.') + 1);
        FileOutputStream out;
        try {
            out = new FileOutputStream(filename);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Uses the Huffman tree to decode a file
    // Takes in a file and and a tree and creates a new file with the original extension found in the file extension.
    // (i.e .HUFFTXT -> .txt)
    public void decode(String filename, HashMap hashmap) {
        // gets the file extension of filename
        String ext = filename.substring(filename.lastIndexOf('.') + 1);
    }
}
