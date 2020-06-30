package core.entity;

import util.encoder.Adler32Util;
import util.encoder.Md5Util;

public enum SyncCategory {
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
