package core.value;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import core.entity.DuneFile;
import core.entity.IDuneFile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:03
 * @description : 增量文件
 */
public class DeltaFile implements IDeltaFile {

    // private final int Id;

    /**
     * 增量文件的ByteBuf组合试图，因为某些重复部分不会以byte[]形式出现
     */

    private final List<DeltaFileEntry> entries;

    private DeltaFile (DeltaFileBuilder builder){
        entries = builder.entries;

    }

    public static class DeltaFileBuilder {
        private List<DeltaFileEntry> entries;


        public DeltaFileBuilder setIsMatch(List<DeltaFileEntry> entries) {
            this.entries = entries;
            return this;
        }

        public DeltaFile build(){
            return new DeltaFile(this);
        }
    }

    public static DeltaFileBuilder builder(){
        return new DeltaFileBuilder();
    }

    public List<DeltaFileEntry> getEntries() {
        return entries;
    }

    @Override
    public Iterator<DeltaFileEntry> iterator() {
        return entries.iterator();
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
