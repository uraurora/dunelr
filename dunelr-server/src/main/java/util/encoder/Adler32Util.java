package util.encoder;

import java.nio.charset.StandardCharsets;
import java.util.zip.Adler32;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-23 16:09
 * @description :
 */
public abstract class Adler32Util {

    public static byte[] encodeToBytes(byte[] bytes){
        return ConvertUtil.long2Bytes(encodeToLong(bytes));
    }

    public static long encodeToLong(byte[] bytes){
        long res = 0;
        try {
            Adler32 adler32 = new Adler32();
            adler32.update(bytes);
            res = adler32.getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static byte[] encodeToBytes(String str){
        return encodeToBytes(str.getBytes(StandardCharsets.UTF_8));
    }


    public static void main(String[] args) {
        // System.out.println(Arrays.toString(encodeToBytes("hello myslefasds a sdasdasdadaasdasdasdasdasdasdasdasdasdasdasdasd")));
        // System.out.println(encodeToLong("hello myself".getBytes(StandardCharsets.UTF_8)));
    }
}
