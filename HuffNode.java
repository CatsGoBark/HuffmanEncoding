package cs146;

public class HuffNode implements Comparable<HuffNode>{

    String code = "";               // The huffman code (i.e 010)
    HuffNode left, right, parent;
    Byte aByte;                     // The byte (i.e FF)
    int frequency;                  // The frequency used for huffman tree creation (Unnecessary?)

    // Creates a node with aByte
    public HuffNode(Byte txt, int freq) {
        this.aByte = txt;
        this.frequency = freq;
    }

    // Creates an "empty" node
    public HuffNode(int freq) {
        this.frequency = freq;
    }

    @Override
    public int compareTo(HuffNode n) {
        if (frequency < n.frequency)
            return -1;
        else if(frequency > n.frequency)
            return 1;
        return 0;
    }

    // Returns true if node is a leaf.
    public boolean isLeaf() {
        return ((left == null) && (right == null));
    }

    @Override
    // Returns the tree as a string.
    // Used to write to the file or as a separate file to decode it later
    public String toString() {
        return null;
    }

    // Creates a tree from a string
    // Used to recreate the tree used to encode the file to decode it.
    public static HuffNode getTreeFromString(String str) {
        return null;
    }

    // Needs work if it's ever used
    /*public static void print(HuffNode n, String line) {
        // print with colon if leaf node
        if (n.aByte == null)
            System.out.println(line + n.frequency + ":" + n.aByte);
        else
            System.out.println(line + n.frequency);
        // Start recursive on left child then right
        if (n.left != null)
            print(n.left, line + "-");
        if (n.right != null)
            print(n.right, line + "-");
    }*/

}