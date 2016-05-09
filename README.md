# HuffmanEncoding
Compresses files using the Huffman Algorithm by reading and counting individual bytes

Here's the process basically.

1. Get the file to be converted

2. Get the counts for the number of bytes (HuffmanCoder.getQuantityCountMap) 

3. Create Huffman tree from ^

4. Create another Hashmap<Byte, String> with the encoding bits from the Tree to use for encoding and decoding (i.e 00 = "010", AF = "1", etc)

5. Encode

  a. Write the tree to the file first (NO IDEA HOW)
  
  b. Write the encoded bits to the file using the buildmap Hashmap.
  
  c. Save it as .HUFFxxx  (i.e .txt -> .HUFFTXT)
  

6. Decode

  a. Read the first line which SHOULD have the tree used to encode the file ^ (AGAIN, NO IDEA HOW)
  
  b. Recreate the tree
  
  c. Recreate the original file using the encoding thing. This is basically a simple Caesar cipher using the decodeMap Hashmap.
  https://en.wikipedia.org/wiki/Caesar_cipher
  
  d. Save it as the original filename. File extension should have been saved in the encoded filename (i.e .HUFFTXT -> .txt)
  

