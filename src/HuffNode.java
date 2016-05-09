public class HuffNode implements Comparable<HuffNode>{

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
        return frequency - n.frequency;
    }

    // Returns true if node is a leaf.
    public boolean isLeaf() {
        return ((left == null) && (right == null));
    }

    // Returns the tree as a string.
    // Used to write to the file or as a separate file to decode it later
    // NEEDS WORK
    @Override
    public String toString() {
        return "HuffNode{" +
                "aByte=" + aByte +
                ", frequency=" + frequency +
                '}';
    }


    // Creates a tree from a string
    // Used to recreate the tree used to encode the file to decode it.
    public static HuffNode getTreeFromString(String str) {
        return null;
    }
}
