package connect.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-17 15:00
 * @description : 处理文件上传与下载请求
 */
public class DunelrServerHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private DunelrServerHttpRequestHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        final HttpMethod method = request.method();
        // 处理100continue请求以适配Http1.1协议
        if (HttpUtil.is100ContinueExpected(request)) {
            send100Continue(ctx);
        }

        // 处理bad request
        if (request.decoderResult().isSuccess()) {
            // 处理请求方法
            FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), OK);
            if (method.equals(HttpMethod.POST)){
                handlePost(ctx, request, response);
            }else if(method.equals(HttpMethod.GET)){
                handleGet(ctx, request, response);
            } else {
                sendHttpResponse(ctx, request, new DefaultFullHttpResponse(
                        request.protocolVersion(),
                        FORBIDDEN,
                        ctx.alloc().buffer(0))
                );
            }
        } else{
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(
                    request.protocolVersion(),
                    BAD_REQUEST,
                    ctx.alloc().buffer(0))
            );
        }
    }

    private void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        final HttpHeaders headers = request.headers();
        // TODO:分析request，看是summary还是delta还是整个文件，构造返回结果，更新服务端状态
        // response.headers().set(HttpHeaderNames.CONTENT_TYPE, );
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        final ByteBuf content = request.content();
        HttpUtil.setContentLength(response, content.readableBytes());
        ByteBufUtil.writeUtf8(response.content(), response.toString());
        sendHttpResponse(ctx, request, response);
    }

    private void handleGet(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        final HttpHeaders headers = request.headers();
        // TODO:分析请求文件的类型，构造文件，更新状态，返回结果
        sendHttpResponse(ctx, request, response);
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        // Generate an error page if response getStatus code is not OK (200).
        HttpResponseStatus responseStatus = response.status();
        // Send the response and close the connection if necessary.
        boolean keepAlive = HttpUtil.isKeepAlive(request) && responseStatus.equals(OK);
        HttpUtil.setKeepAlive(response, keepAlive);
        ChannelFuture future = ctx.writeAndFlush(response);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, CONTINUE);
        ctx.writeAndFlush(response);
    }

    public static DunelrServerHttpRequestHandler newInstance(){
        return new DunelrServerHttpRequestHandler();
    }
}
