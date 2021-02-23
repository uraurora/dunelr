package util.buffer;

import io.netty.buffer.ByteBuf;

import java.util.concurrent.Callable;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-24 10:15
 * @description :
 */
public abstract class MyByteBufUtil {

    public static WrapperArray handleArray(ByteBuf buf){
        byte[] array;
        int offset, length;
        if (buf.hasArray()){
            // 支撑数组形式的话
           array = buf.array();
           // 计算第一个字节的偏移量
           offset = buf.arrayOffset() + buf.readerIndex();
           // 获取可读字节数
           length = buf.readableBytes();
        } else {
            // 直接缓冲区形式，获取可读字节数
            offset = 0;
            length = buf.readableBytes();
            // 分配一个数组保存具有该长度的字节数据
            array = new byte[length];
            // 复制数据
            buf.getBytes(buf.readerIndex(), array);
        }
        return new WrapperArray(array, offset, length);
    }

    public static class WrapperArray{
        private final byte[] array;

        private final int offset;

        public WrapperArray(byte[] array, int offset, int length) {
            this.array = array;
            this.offset = offset;
            this.length = length;
        }

        public byte[] getArray() {
            return array;
        }

        public int getOffset() {
            return offset;
        }

        public int getLength() {
            return length;
        }

        private final int length;


    }

}
