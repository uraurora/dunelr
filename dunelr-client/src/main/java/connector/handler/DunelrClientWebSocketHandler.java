package connector.handler;

import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.*;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-17 15:02
 * @description : 处理websocket二进制数据帧的handler
 */
public class DunelrClientWebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private DunelrClientWebSocketHandler(){}

    public static DunelrClientWebSocketHandler newInstance(){
        return new DunelrClientWebSocketHandler();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
//        if(msg instanceof BinaryWebSocketFrame){
//            handleBinaryWebSocketFrame(ctx, (BinaryWebSocketFrame) msg);
//        }
//        else if(msg instanceof TextWebSocketFrame){
//            handleTextWebSocketFrame(ctx, (TextWebSocketFrame) msg);
//        }
        // 测试
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
            System.out.println("WebSocket Client received message: " + textFrame.text());
        } else if (msg instanceof PongWebSocketFrame) {
            System.out.println("WebSocket Client received pong");
        } else if (msg instanceof CloseWebSocketFrame) {
            System.out.println("WebSocket Client received closing");
            ctx.channel().close();
        }
    }

    private void handleTextWebSocketFrame(ChannelHandlerContext ctx, TextWebSocketFrame msg) {

    }

    private void handleBinaryWebSocketFrame(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) {

    }
}
