package core.entity;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import core.value.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import util.encoder.ConvertUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static DuneFile newInstance(Path path) throws IOException {
        return newInstance(path, SyncCategory.RSYNC);
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
        return DuneFileSummary.newInstance(blocks());
    }

    /**
     * 和另一个文件比对生成增量文件，本身为source，other为dst文件摘要
     * @param other 另一个抽象文件摘要
     * @return 增量文件
     * @throws IOException
     */
    @Override
    public IDeltaFile delta(DuneFileSummary other) throws IOException {
        int left = 0, start = 0;
        byte[] bytes = new byte[DuneBlock.SIZE];
        List<DeltaFileEntry> entries = Lists.newArrayList();
        Map<Long, DuneBlock> map = weakCheckSumMap(other);

        try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")){
            while(left < size()){
                raf.seek(left);
                raf.read(bytes);

                // 判断弱校验是否匹配
                long weakCheck = syncCategory.weakCheck(bytes);
                if (map.containsKey(weakCheck)) {
                    DuneBlock block = map.get(weakCheck);
                    // 强校验是否匹配，匹配则记录文件块index
                    if (Arrays.equals(syncCategory.strongCheck(bytes), block.getStrongCheckSum())) {
                        // 首先记录之前未匹配的数据块
                        if(start < left){
                            byte[] temp = new byte[left - start];
                            raf.seek(start); raf.read(temp);
                            entries.add(new DeltaFileEntry(Unpooled.wrappedBuffer(temp), false));
                        }
                        // deltaFile记录匹配和index
                        entries.add(new DeltaFileEntry(Unpooled.copiedBuffer(ConvertUtil.int2Bytes(block.getIndex())), true));
                        left += DuneBlock.SIZE;
                        start = left;
                    } else{
                        // 弱检验发生碰撞，仍旧未通过
                        left ++;
                    }
                } else{
                    // 弱校验不通过表示block不匹配，则left++窗口滑动，未匹配数据终止位++
                    left ++;
                }
            }
            if (start < left){
                byte[] temp = new byte[left - start];
                raf.seek(start); raf.read(temp);
                entries.add(new DeltaFileEntry(Unpooled.wrappedBuffer(temp), false));
            }
        }
        return DeltaFile.builder()
                .setIsMatch(entries)
                .build();
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
        try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")){
            while(cursor < blockNum){
                raf.read(bytes);
                // 构造文件块不可变对象
                DuneBlock block = DuneBlock.builder()
                        .setIndex(cursor)
                        .setWeakCheckSum(syncCategory.weakCheck(bytes))
                        .setStrongCheckSum(syncCategory.strongCheck(bytes))
                        .build();
                res.add(block);
                cursor++;
            }
        }

        return res;
    }

    private static Map<Long, DuneBlock> weakCheckSumMap(DuneFileSummary summary){
        return summary.toBlocks().stream().collect(Collectors.toMap(
                DuneBlock::getWeakCheckSum,
                e->e,
                (v1, v2) -> v2
        ));
    }
}
