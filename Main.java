package cs146;

public class Main {

    public static void main(String[] args) {
	String filename = "args[0]";
	HuffmanCoder test = new HuffmanCoder(filename);
        test.getQuantityHashMap();
        test.makeHuffManTree();
        test.buildCode();
        test.encode(filename, test.buildMap);
        test.decode(HuffmanCoder.getHuffFilename(filename), test.decodeMap);
    }
}
