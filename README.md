# HuffmanEncoding
Compresses files using the Huffman Algorithm by reading and counting individual bytes as 2 hex characters 

(i.e 11111111 -> FF).

Here's the process generally. There's probably flaws:
1. Get the file to be converted
2. Get the counts for the number of bytes (HuffmanCoder.getQuantityCountMap) 
3. Create Huffman tree from ^
4. Create another Hashmap<Byte, String?> with the encoding bits from the Tree for ease of use? (i.e 00 = "010", AF = "1", etc)
5. Encode
  a. Write the tree to the file first
  b. Write the encoded bits to the file.
  c. Save it as .HUFFxxx  (i.e .txt -> .HUFFTXT)

6. Decode
  a. Read the first line which SHOULD have the tree used to encode the file ^
  b. Recreate the tree
  c. Recreate the original file using the encoding thing. This is basically a simple Caesar cipher. https://en.wikipedia.org/wiki/Caesar_cipher
  d. Save it as the original filename. File extension should have been saved in the encoded filename (i.e .HUFFTXT -> .txt)

