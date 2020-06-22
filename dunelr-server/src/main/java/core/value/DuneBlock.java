package core.value;

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
    public static final int SIZE = 2 * 1024;
    /**
     * 文件块的索引，在文件中的内部索引
     */
    private final int index;
    /**
     * 文件块的大小，这个是固定值
     */
    private final byte[] data;

    // TODO: hash索引和加密值

    private DuneBlock (DuneBlockBuilder builder){
        index = builder.index;
        data = builder.data;
    }

    public static class DuneBlockBuilder {
        private int index;

        private byte[] data;

        public DuneBlockBuilder setIndex(int index) {
            this.index = index;
            return this;
        }

        public DuneBlockBuilder setData(byte[] data) {
            this.data = data;
            return this;
        }

        public DuneBlock build(){
            return new DuneBlock(this);
        }
    }

    public static DuneBlockBuilder builder(){
        return new DuneBlockBuilder();
    }

}
