
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 *  Topics touched. Huffman encoding/decoding, trees, priority queues, HashMaps.
 *
 */
public class HuffmanCoder {

    private final static int BUFFER_SIZE = 512; //Allows for more efficient reading
    private String filename;
    private HashMap<Byte, Integer> countMap;           //Stores the byte quantity count for a file
    private HuffCode[] buildArray;
    private HuffCode[] decodeArray;
    private int decodeIndex;

    private HuffNode huffTree;                         //Stores the generated Huffman Tree

    private final static int[] TWO_MASK = {0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32766, 65535};

    public HuffmanCoder(String filename) {
        this.filename = filename;
    }
    
    // Demonstrates what the program does.
    // Basically outlines the process.
    public static void demo(String filename) {
        Instant start, end;

        HuffmanCoder test = new HuffmanCoder(filename);                         //Get filename and create HuffmanCoder instance

        start = Instant.now();
        test.getQuantityHashMap();                                              //Analyze file
        end = Instant.now();

        System.out.printf("Analyze File: %s\n", Duration.between(start, end));

        start = Instant.now();
        test.makeHuffManTree();                                                 //Create Huffman tree
        end = Instant.now();

        System.out.printf("Huffman Tree Creation: %s\n", Duration.between(start, end));

        start = Instant.now();
        test.buildCode();                                                       //Get the Hashmaps used to encode/decode
        end = Instant.now();

        System.out.printf("Build HashMap: %s\n", Duration.between(start, end));

        start = Instant.now();
        test.encode(filename);                                   //encode test
        end = Instant.now();

        System.out.printf("Encode File: %s\n", Duration.between(start, end));

//        System.exit(0);

        start = Instant.now();
        test.decode(HuffmanCoder.getHuffFilename(filename));    //decode test
        end = Instant.now();

        System.out.printf("Decode File: %s\n", Duration.between(start, end));
    }

    // Creates an empty file in the current directory
    // Takes in a file name including file type as a parameter.
    private static void createFile(String name) {
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
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // Prints contents of of a HashMap
    // Uses for testing purposes only.
    private static <T> void printHashMap(HashMap<Byte, T> map) {
        map.forEach((k, v) -> {
            System.out.printf("%x = %s\n", k, v);
        });
    }

    public int getBufferSize() {
        return 0;
    }

    // Helper method
    public void makeHuffManTree() {
        this.huffTree = makeHuffmanTree(countMap);
    }

    // Creates a huffman tree from quantity HashMap.
    private static HuffNode makeHuffmanTree(HashMap<Byte, Integer> hashmap) {
        PriorityQueue<HuffNode> queue = new PriorityQueue<>();
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
        this.buildArray = new HuffCode[256];
        this.decodeArray = new HuffCode[256];
        this.decodeIndex = 0;
        buildCode(this.huffTree, "");

        Arrays.sort(this.decodeArray, 0, this.decodeIndex, (o1, o2) -> (o1.getByte() - o2.getByte() != 0) ? o1.getByte() - o2.getByte() : o1.getSize() - o2.getSize());
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
            HuffCode code = new HuffCode(s, x.aByte);
            buildArray[x.aByte + 128] = code;
            decodeArray[this.decodeIndex++] = code;
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
    
    public void encode(String filename) {
        HuffmanCoder.createFile(HuffmanCoder.getHuffFilename(filename));
        File encodedFile = new File(HuffmanCoder.getHuffFilename(filename));
        FileOutputStream out;
        DataInputStream dis;
        try {
            out = new FileOutputStream(encodedFile);
            dis = new DataInputStream(new FileInputStream(filename));

            byte[] buffer = new byte[BUFFER_SIZE];
            byte[] out_buffer = new byte[BUFFER_SIZE];
            int out_idx = 0;
            int length;

            HuffCode code;
            int size = 0;
            int data = 0;

            while ((length = dis.read(buffer)) != -1) {
                for (int i=0; i < length; i++) {
                    code = buildArray[buffer[i] + 128];

                    data <<= code.getSize();
                    data |= code.getByte();

                    size += code.getSize();

                    while (size >= 8) {
                        size -= 8;
                        out_buffer[out_idx++] = (byte) (data >> size);
                        data &= TWO_MASK[size];

                        if (out_idx == BUFFER_SIZE) {
                            out.write(out_buffer);
                            out_idx = 0;
                        }
                    }
                }
            }

            out.write(out_buffer, 0, out_idx);

            int padding = 0;

            if (size != 0) {
                padding = 8 - size;
                data = data << padding;
                out.write(data);
            }

            System.out.printf("Size: %d Data: %x Padding: %d\n", size, data, padding);

            out.write(padding);

            dis.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Uses the Huffman tree to decode a file
    // New file is created in the same directory as the encoded file
    // Takes in a file and and a reverse build HashMap
    
    // ISSUE: See encoding.

    private int indexOfCode(int target, int size) {
        return Arrays.binarySearch(this.decodeArray, 0, this.decodeIndex, new HuffCode(target, size), (o1, o2) -> (o1.getByte() - o2.getByte() != 0) ? o1.getByte() - o2.getByte() : o1.getSize() - o2.getSize());
    }
    
    public void decode(String filename) {
        HuffmanCoder.createFile(HuffmanCoder.getDecodedFilename(filename));

        File decodedFile = new File(HuffmanCoder.getDecodedFilename(filename));

        FileOutputStream out;
        DataInputStream dis;

        try {
            out = new FileOutputStream(decodedFile);
            dis = new DataInputStream(new FileInputStream(filename));

            byte[] buffer = new byte[BUFFER_SIZE];
            byte[] out_buffer = new byte[BUFFER_SIZE];
            int length;
            int out_idx = 0;

            int data = 0;
            int size = 0;
            int index;
            HuffCode code;

            while ((length = dis.read(buffer)) != -1) {

                for (int i=0; i < length; i++) {
                    int offset = 7;
                    while (offset >= 0) {
                        data <<= 1;
                        size += 1;
                        data |= buffer[i] >> offset & 0x1;
                        offset -= 1;

                        index = this.indexOfCode(data, size);

                        if (index >= 0) {
                            // found target code
                            code = this.decodeArray[index];
                            out_buffer[out_idx++] = code.getaByte();

                            if (out_idx == BUFFER_SIZE) {
                                out.write(out_buffer);
                                out_idx = 0;
                            }

                            data = 0;
                            size = 0;
                        }
                    }
                }
            }

            if (out_idx != 0) {
                out.write(out_buffer, 0, out_idx);
            }

            dis.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
