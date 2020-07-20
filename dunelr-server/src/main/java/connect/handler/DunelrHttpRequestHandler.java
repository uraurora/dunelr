package connect.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-17 15:00
 * @description :
 */
public class DunelrHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private DunelrHttpRequestHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        HttpHeaders headers = request.headers();
        ByteBuf content = request.content();
        final String uri = request.uri();
        final HttpMethod method = request.method();

        if (method.equals(HttpMethod.POST)){
            final String s = headers.get(HttpHeaderNames.CONTENT_LENGTH);
        }
        // 处理100continue请求以适配Http1.1协议
        if (HttpUtil.is100ContinueExpected(request)) {
            send100Continue(ctx);
        }
        HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");


        boolean keepAlive = HttpUtil.isKeepAlive(request);
        // 如果请求了keep-alive，则添加所需要的http头信息
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 22);
            response.headers().set( HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        // 将响应消息写到客户端
        ctx.write(response);// 写LastHttpContent并冲刷到客户端
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        // 如果没有请求keep-alive则写完之后关闭channel
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    public static DunelrHttpRequestHandler newInstance(){
        return new DunelrHttpRequestHandler();
    }
}
