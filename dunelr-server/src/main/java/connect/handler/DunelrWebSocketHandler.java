package connect.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-17 15:01
 * @description : 处理websocket二进制数据帧的handler
 */
public class DunelrWebSocketHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

    private DunelrWebSocketHandler(){}

    public static DunelrWebSocketHandler newInstance(){
        return new DunelrWebSocketHandler();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {

    }
}
