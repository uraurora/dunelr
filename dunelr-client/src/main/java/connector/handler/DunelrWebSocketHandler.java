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
public class DunelrWebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if(msg instanceof BinaryWebSocketFrame){
            handleBinaryWebSocketFrame(ctx, msg);
        }
        else if(msg instanceof TextWebSocketFrame){
            handleTextWebSocketFrame(ctx, msg);
        }
    }

    private void handleTextWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {

    }

    private void handleBinaryWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {

    }
}
