package connector.handler;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-17 15:02
 * @description : 处理websocket二进制数据帧的handler
 */
public class DunelrWebSocketHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {

    }
}
