package core.value;

import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.CompositeByteBuf;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:03
 * @description : 增量文件
 */
public class DeltaFile implements IDeltaFile {

    /**
     * 增量文件的ByteBuf组合试图，因为某些重复部分不会以byte[]形式出现
     */
    private final CompositeByteBuf buf;

    private final boolean[] isMatch;

    private DeltaFile (DeltaFileBuilder builder){
        buf = builder.buf;
        isMatch = builder.isMatch;
    }

    public static class DeltaFileBuilder {
        private CompositeByteBuf buf;

        private boolean[] isMatch;


        public DeltaFileBuilder setIndex(CompositeByteBuf buf) {
            this.buf = buf;
            return this;
        }

        public DeltaFileBuilder setIsMatch(boolean[] isMatch) {
            this.isMatch = isMatch;
            return this;
        }

        public DeltaFile build(){
            return new DeltaFile(this);
        }
    }

    public static DeltaFileBuilder builder(){
        return new DeltaFileBuilder();
    }


    @Override
    public IDeltaFile plus(IDeltaFile other) {
        return null;
    }

    @Override
    public IDeltaFile subtract(IDeltaFile other) {
        return null;
    }
}
