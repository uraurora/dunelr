package connect.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-17 15:00
 * @description :
 */
public class DunelrHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private DunelrHttpRequestHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

    }

    public static DunelrHttpRequestHandler newInstance(){
        return new DunelrHttpRequestHandler();
    }
}
