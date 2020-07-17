package file.value.context;

import file.value.entry.DeltaEntry;

import java.util.Iterator;
import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:03
 * @description : 增量文件
 */
public class Delta implements IDelta {

    // private final int Id;

    /**
     * 增量文件的ByteBuf组合试图，因为某些重复部分不会以byte[]形式出现
     */

    private final List<DeltaEntry> entries;

    private Delta(DeltaFileBuilder builder){
        entries = builder.entries;

    }

    public static class DeltaFileBuilder {
        private List<DeltaEntry> entries;


        public DeltaFileBuilder setEntries(List<DeltaEntry> entries) {
            this.entries = entries;
            return this;
        }

        public Delta build(){
            return new Delta(this);
        }
    }

    public static DeltaFileBuilder builder(){
        return new DeltaFileBuilder();
    }

    @Override
    public List<DeltaEntry> getEntries() {
        return entries;
    }

    @Override
    public Iterator<DeltaEntry> iterator() {
        return entries.iterator();
    }



    @Override
    public IDelta plus(IDelta other) {
        return null;
    }

    @Override
    public IDelta subtract(IDelta other) {
        return null;
    }

}
