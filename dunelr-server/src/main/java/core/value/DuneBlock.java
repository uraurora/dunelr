package core.value;

import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:16
 * @description : Dune文件的文件分块
 */
public class DuneBlock {
    /**
     * 文件块的大小，一般默认为2KB
     */
    public static final int SIZE =  2 * 1024;
    /**
     * 文件块的索引，在文件中的内部索引
     */
    private final int index;

    /**
     * adler-32算法，32位，主要用来区别不同
     */
    private final long weakCheckSum;
    /**
     * hash md5加密，128bit，16byte，避免adler-32的碰撞，确认相同
     */
    private final byte[] strongCheckSum;

    private DuneBlock (DuneBlockBuilder builder){
        index = builder.index;
        weakCheckSum = builder.weakCheckSum;
        strongCheckSum = builder.strongCheckSum;
    }

    public static class DuneBlockBuilder {
        private int index;

        private long weakCheckSum;

        private byte[] strongCheckSum;

        public DuneBlockBuilder setIndex(int index) {
            this.index = index;
            return this;
        }

        public DuneBlockBuilder setWeakCheckSum(long weakCheckSum) {
            this.weakCheckSum = weakCheckSum;
            return this;
        }

        public DuneBlockBuilder setStrongCheckSum(byte[] strongCheckSum) {
            this.strongCheckSum = strongCheckSum;
            return this;
        }



        public DuneBlock build(){
            return new DuneBlock(this);
        }
    }

    public static DuneBlockBuilder builder(){
        return new DuneBlockBuilder();
    }

    public int getIndex() {
        return index;
    }

    public long getWeakCheckSum() {
        return weakCheckSum;
    }

    public byte[] getStrongCheckSum() {
        return strongCheckSum;
    }

    public static void main(String[] args) {
        LocalDate date1 = LocalDate.parse("1582-10-04"), date2 = LocalDate.parse("1582-10-15");
        // System.out.println(ChronoUnit.DAYS.between(date1, date2));
    }

}
