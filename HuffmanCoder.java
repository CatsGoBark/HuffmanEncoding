
import java.io.*;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 *  Topics touched. Huffman encoding/decoding, trees, priority queues, HashMaps.
 *
 */
public class HuffmanCoder {

    public final static int BUFFER_SIZE = 512; //Allows for more efficient reading
    private String filename;
    HashMap<Byte, Integer> countMap;           //Stores the byte quantity count for a file
    HashMap<Byte, String> buildMap;            //The map used to encode
    HashMap<String, Byte> decodeMap;           //The map used to decode. This is basically a reversed version of buildmap.
    HuffNode huffTree;                         //Stores the generated Huffman Tree

    public HuffmanCoder(String filename) {
        this.filename = filename;
    }
    
    // Demonstrates what the program does.
    // Basically outlines the process.
    public static void demo(String filename) {
        HuffmanCoder test = new HuffmanCoder(filename);                         //Get filename and create HuffmanCoder instance
        test.getQuantityHashMap();                                              //Analyze file
        test.makeHuffManTree();                                                 //Create Huffman tree
        test.buildCode();                                                       //Get the Hashmaps used to encode/decode
        test.encode(filename, test.buildMap);                                   //encode test
        test.decode(HuffmanCoder.getHuffFilename(filename), test.decodeMap);    //decode test
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

    // Counts the number of kinds of Bytes and stores it into countMap
    // Byte is the key and the quantity is the value
    // Run this before running the huffman algorithm. It uses countMap to generate the tree.
    public void getQuantityHashMap() {
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(this.filename));
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

    // Prints contents of of a HashMap
    // Uses for testing purposes only.
    public static <T> void printHashMap(HashMap<Byte, T> map) {
        map.forEach((k, v) -> {
            System.out.printf("%x = %s\n", k, v);
        });
    }

    // Helper method
    public void makeHuffManTree() {
        this.huffTree = makeHuffmanTree(countMap);
    }

    // Creates a huffman tree from quantity HashMap.
    private static HuffNode makeHuffmanTree(HashMap<Byte, Integer> hashmap) {
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

    // Helper method
    public void buildCode() {
        this.buildMap = new HashMap<Byte, String>();
        this.decodeMap = new HashMap<String, Byte>();
        buildCode(this.huffTree, "");
    }

    // Creates a HashMap from the huffman tree used to decode and encode files.
    private void buildCode(HuffNode x, String s) {
        if (x == null)
            return;
        if (!x.isLeaf()) {
            buildCode(x.left, s + "0");
            buildCode(x.right, s + "1");
        }
        else {
            buildMap.put(x.aByte, s);
            decodeMap.put(s, x.aByte);
        }
    }
    // Gets the encoded filename from a normal filename (i.e test.txt -> test.hufftxt)
    public static String getHuffFilename(String filename) {
        return filename.substring(0, filename.lastIndexOf('.') + 1) + "huff" + filename.substring(filename.lastIndexOf('.') + 1);
    }

    // Gets the normal filename from an encoded filename (i.e test.hufftxt -> test.txt)
    public static String getNormFilename(String filename) {
        return filename.substring(0, filename.lastIndexOf(".huff") + 1) + filename.substring(filename.indexOf(".huff") + 5);
    }

    // Gets the decoded filename from an encoded filename (i.e test.hufftxt -> test-decoded.txt)
    public static String getDecodedFilename(String filename) {
        String name = (filename.substring(0, filename.lastIndexOf('.')) + "-decoded");
        name += "." + filename.substring(filename.indexOf(".huff") + 5);
        return name;
    }

    // Uses the Huffman tree to encode a file.
    // New file is created in the same directory as the original file.
    // Takes a file to encode and the build hashmap to encode it.
    
    // HUGE ISSUEEEE: If the final file does not end in a perfect byte (i.e 8 bits 00110011 vs. 001 or 00101 <8 bits), it doesn't write it.
    // This end ups having the encoded file MISSING the last byte or so from files.
    // SOMETIMES, this is okay but most of the time it makes the file corrupted.
    // On regular .txt files, it results in missing the last character or so.
    // SOMEONE PLEASE THINK OF A WORKAROUND.
    
    public void encode(String filename, HashMap hashmap) {
        HuffmanCoder.createFile(HuffmanCoder.getHuffFilename(filename));
        File encodedFile = new File(HuffmanCoder.getHuffFilename(filename));
        FileOutputStream out;
        DataInputStream dis;
        try {
            out = new FileOutputStream(encodedFile);
            String toByte = "";
            dis = new DataInputStream(new FileInputStream(filename));
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = dis.read(buffer)) != -1) {
                for (int i=0; i< length; i++) {
                    toByte += buildMap.get(buffer[i]);
                }
                //Testing purposes. Prints our the build code for the file.
                //Should be the same as the one printed in encode
                //System.out.println(toByte);
                while (toByte.length() >= 8) {
                    Byte b = (byte)Integer.parseInt(toByte.substring(0,8), 2);
                    out.write(b);
                    toByte = toByte.substring(8);
                }
            }

            dis.close();
            out.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Uses the Huffman tree to decode a file
    // New file is created in the same directory as the encoded file
    // Takes in a file and and a reverse build HashMap
    
    // ISSUE: See encoding.
    
    public void decode(String filename, HashMap hashmap) {
        HuffmanCoder.createFile(HuffmanCoder.getDecodedFilename(filename));
        File decodedFile = new File(HuffmanCoder.getDecodedFilename(filename));
        FileOutputStream out;
        DataInputStream dis;
        try {
            out = new FileOutputStream(decodedFile);
            String toCode = "";
            dis = new DataInputStream(new FileInputStream(filename));
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = dis.read(buffer)) != -1) {
                for (int i=0; i< length; i++) {
                    toCode += String.format("%8s", Integer.toBinaryString(buffer[i] & 0xFF)).replace(' ', '0');
                }
                //System.out.println(toCode);
                int offset = 2;
                while (offset < toCode.length()) {
                    if (decodeMap.containsKey(toCode.substring(0,offset))) {
                        out.write(decodeMap.get(toCode.substring(0, offset)));
                        toCode = toCode.substring(offset);
                        offset = 2;
                    }
                    else
                        offset++;
                }
            }
            dis.close();
            out.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
