package enums;

import util.encoder.Adler32Util;
import util.encoder.Md5Util;

/**
 * @author gaoxiaodong
 */

public enum CheckSumCategory {
    /**
     * rsync同步算法
     */
    RSYNC{
        @Override
        public long weakCheck(byte[] bytes) {
            return Adler32Util.encodeToLong(bytes);
        }

        @Override
        public byte[] strongCheck(byte[] bytes) {
            return Md5Util.encodeToBytes(bytes);
        }

    },

    ;

    public abstract long weakCheck(byte[] bytes);

    public abstract byte[] strongCheck(byte[] bytes);

}
