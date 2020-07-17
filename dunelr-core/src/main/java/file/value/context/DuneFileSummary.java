package file.value.context;

import file.value.entry.DuneBlock;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-24 11:06
 * @description : 文件分块后的checkSum汇总，包含文件每个块的强弱校验，也是传输对象，务必考虑性能
 */
public class DuneFileSummary {

    private final List<DuneBlock> blocks;

    private DuneFileSummary(List<DuneBlock> blocks){
        this.blocks = blocks;
    }

    public static DuneFileSummary newInstance(List<DuneBlock> blocks){
        return new DuneFileSummary(blocks);
    }

    public List<DuneBlock> toBlocks(){
        return blocks;
    }

    public CompositeByteBuf toCompositeByteBuf(){
        CompositeByteBuf bufs = Unpooled.compositeBuffer();
        for (DuneBlock block : blocks){
            ByteBuf buf = Unpooled.buffer()
                    .writeInt(block.getIndex())
                    .writeLong(block.getWeakCheckSum())
                    .writeBytes(block.getStrongCheckSum());
            bufs.addComponent(buf);
        }
        return bufs;
    }


}
