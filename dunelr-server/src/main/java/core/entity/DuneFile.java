package core.entity;

import com.google.common.collect.Lists;
import core.value.*;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import util.encoder.ConvertUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

    private final static String DUNE_FILE_TEMP_PRE = "~dune-temp";

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
    public IDelta delta(DuneFileSummary other) throws IOException {
        int left = 0, start = 0;
        byte[] bytes = new byte[DuneBlock.SIZE];
        List<DeltaEntry> entries = Lists.newArrayList();
        Map<Long, DuneBlock> map = weakCheckSumMap(other.toBlocks());

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
                            entries.add(new DeltaEntry(Unpooled.wrappedBuffer(temp), false));
                        }
                        // deltaFile记录匹配和index
                        entries.add(new DeltaEntry(Unpooled.wrappedBuffer(ConvertUtil.int2Bytes(block.getIndex())), true));
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
                entries.add(new DeltaEntry(Unpooled.wrappedBuffer(temp), false));
            }
        }
        return Delta.builder()
                .setIsMatch(entries)
                .build();
    }

    /**
     * 增量更新文件的方法，会重新生成文件
     * @param deltaFile delta文件
     * @return 新的文件对象
     */
    @Override
    public IDuneFile plus(IDelta deltaFile) throws IOException {
        // FIXME:完成文件整合，首先应当根据delta中的匹配个数来判断是是否文件整合，文件整合是否必须是新建方式
        if (deltaFile.getEntries().stream().allMatch(DeltaEntry::isBool)) {
            return this;
        } else {
            Path tempPath =path.getParent().resolve(DUNE_FILE_TEMP_PRE + path.getFileName());
            try(final OutputStream outputStream = Files.newOutputStream(
                    tempPath,
                    StandardOpenOption.CREATE)
            ) {
                try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {

                    for (DeltaEntry e : deltaFile) {
                        // 文件块匹配，写入本地文件块
                        if (e.isBool()) {
                            int index = ConvertUtil.bytes2Int(ByteBufUtil.getBytes(e.getBuf()));
                            raf.seek(DuneBlock.SIZE * index);
                            byte[] bytes = new byte[DuneBlock.SIZE]; raf.read(bytes);
                            outputStream.write(bytes);
                        } else {
                            // 不匹配则直接写入
                            outputStream.write(ByteBufUtil.getBytes(e.getBuf()));
                        }
                    }
                }
            }
            finally {
                Files.deleteIfExists(path);
                if (Files.exists(tempPath)) {
                    Files.move(tempPath, path);
                }
            }
            return DuneFile.newInstance(path);
        }
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

    private static Map<Long, DuneBlock> weakCheckSumMap(List<DuneBlock> summary){
        return summary.stream().collect(Collectors.toMap(
                DuneBlock::getWeakCheckSum,
                e->e,
                (v1, v2) -> v2
        ));
    }
}
