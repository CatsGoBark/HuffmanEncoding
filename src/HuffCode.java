/**
 * Created by fanzeyi on 5/8/16.
 */
public class HuffCode {
    private String code;
    private int size;
    private int bte;
    private byte aByte;

    public HuffCode(String code, byte aByte) {
        this.code = code;
        this.size = code.length();
        this.bte = Integer.parseInt(this.code, 2);
        this.aByte = aByte;
    }

    public HuffCode(int bte, int size) {
        this.size = size;
        this.bte = bte;
    }

    public String getCode() {
        return code;
    }

    public int getSize() {
        return size;
    }

    public int getByte() {
        return bte;
    }

    public byte getaByte() {
        return aByte;
    }

    public static void main(String[] args) {
        HuffCode code = new HuffCode("100000100", (byte) 0xF);
        System.out.println(code.getByte());
        System.out.println(Integer.parseInt("100000100", 2));
    }
}
