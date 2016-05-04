package cs146;

public class Main {

    public static void main(String[] args) {
	    HuffmanCoder test = new HuffmanCoder(args[0]);
        test.getQuantityHashMap();
        test.printCountMap();
        HuffmanCoder.makeHuffmanTree(test.countMap);
    }
}
