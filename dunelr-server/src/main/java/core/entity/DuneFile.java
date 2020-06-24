package core.entity;

import com.google.common.collect.Lists;
import core.value.DuneBlock;
import core.value.DuneFileSummary;
import core.value.IDeltaFile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:04
 * @description : dunelr的文件抽象
 */
public class DuneFile implements IDuneFile{

    private Path path;

    private int blockNum;

    private SyncCategory syncCategory;

    /**
     * 文件的加密索引，为了能在对方找到一样的文件
     */
    private byte[] fileCheckSum;

    private DuneFile(Path path, SyncCategory category) throws IOException {
        this.path = path;
        this.syncCategory = category;
        int num = (int) size() / DuneBlock.SIZE;
        this.blockNum = size() % DuneBlock.SIZE == 0 ?  num : num + 1;
    }

    public static DuneFile newInstance(Path path) {
        return newInstance(path);
    }

    public static DuneFile newInstance(Path path, SyncCategory category) throws IOException {
        return new DuneFile(path, category);
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public int getBlockNum() {
        return blockNum;
    }

    @Override
    public DuneFileSummary toSummary() throws IOException {
        return null;
    }

    /**
     * 和另一个文件比对生成增量文件，本身为source，other为dst文件摘要
     * @param other 另一个抽象文件摘要
     * @return 增量文件
     * @throws IOException
     */
    @Override
    public IDeltaFile delta(DuneFileSummary other) throws IOException {
        return null;
    }

    /**
     * 会改变自身的方法，会重新生成文件，之后修改
     * @param deltaFile delta文件
     * @return
     */
    @Override
    public IDuneFile plus(IDeltaFile deltaFile) {
        return null;
    }

    private List<DuneBlock> blocks() throws IOException {
        // FIXME:这是读小文件的方法，大文件需要缓存优化(考虑使用ByteBuf的直接缓冲区模式)
        int cursor = 0;
        List<DuneBlock> res = Lists.newArrayList();

        byte[] bytes = new byte[DuneBlock.SIZE];
        ByteBuf buf = Unpooled.copiedBuffer(Files.readAllBytes(path));

        while(cursor < blockNum){
            buf.readBytes(bytes, 0, DuneBlock.SIZE);
            // 构造文件块不可变对象
            DuneBlock block = DuneBlock.builder()
                    .setIndex(cursor)
                    .setWeakCheckSum(syncCategory.weakCheck(bytes))
                    .setStrongCheckSum(syncCategory.strongCheck(bytes))
                    .build();
            res.add(block);
            cursor++;
        }
        return res;
    }
}
