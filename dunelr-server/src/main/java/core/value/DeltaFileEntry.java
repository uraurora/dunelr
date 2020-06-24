package core.value;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-24 15:17
 * @description :
 */
public class DeltaFileEntry {
    private final ByteBuf buf;

    private final boolean bool;

    public DeltaFileEntry(ByteBuf buf, boolean bool) {
        this.buf = buf;
        this.bool = bool;
    }

    public ByteBuf getBuf() {
        return buf;
    }

    public boolean isBool() {
        return bool;
    }

    @Override
    public String toString() {
        return "DeltaFileEntry{" +
                "buf=" + Arrays.toString(buf.array()) +
                ", bool=" + bool +
                '}';
    }
}
