package util.encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-23 14:58
 * @description :
 */
public abstract class Md5Util {
    /**
     * 生成加密摘要，32位
     * @param dataStr 需要加密的字符串
     * @return 摘要
     */
    public static String encodeToString(String dataStr) {
        return encodeToString(dataStr.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成加密摘要，32位
     * @param bytes 需要加密的字符数组
     * @return 摘要
     */
    public static String encodeToString(byte[] bytes) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(bytes);
            byte[] str = m.digest();
            StringBuilder result = new StringBuilder();
            for (byte b : str) {
                result.append(Integer.toHexString((0x000000FF & b) | 0xFFFFFF00).substring(6));
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 生成128bit的16byte的数组
     * @param str 需要加密的字符串
     * @return 摘要
     */
    public static byte[] encodeToBytes(String str){
        return encodeToBytes(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成128bit的16byte的数组
     * @param bytes 需要加密的字符数组
     * @return 摘要
     */
    public static byte[] encodeToBytes(byte[] bytes){
        byte[] res = new byte[0];
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(bytes);
            res = m.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return res;
    }

}
