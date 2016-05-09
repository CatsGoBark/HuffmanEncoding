/**
 * Created by fanzeyi on 5/8/16.
 */
public class HuffCode {
    private String code;
    private int size;
    private int bte;

    public HuffCode(String code) {
        this.code = code;
        this.size = code.length();
        this.bte = Integer.parseInt(this.code, 2);
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

    public static void main(String[] args) {
        HuffCode code = new HuffCode("100000100");
        System.out.println(code.getByte());
        System.out.println(Integer.parseInt("100000100", 2));
    }
}
