package connect.pipeline;

import connect.handler.DunelrServerWebSocketHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-17 15:05
 * @description :
 */
public class DunelrServerInitializer extends ChannelInitializer<Channel> {
    // 如果需要广播到其他的客户端，需要引入一个ChannelGroup传给业务Handler
    private static final String WEBSOCKET_PATH = "/websocket";

    public DunelrServerInitializer() {
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
                // Http服务端的编解码器
                new HttpServerCodec(),
                // 向客户端传输文件的handler，没有加密和压缩的时候效率更高，防止大文件传输的内存溢出
                new ChunkedWriteHandler(),
                // Http内容聚合handler，经过此处理后变为FullHttp
                new HttpObjectAggregator(64 * 1024),
                // 压缩
                new WebSocketServerCompressionHandler(),
                // 处理HttpRequest的业务
                // DunelrHttpRequestHandler.newInstance(),
                // 运行WebSocket服务器大部分工作的handler
                new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true),
                //  处理webSocket相关业务的handler
                DunelrServerWebSocketHandler.newInstance()
        );
    }
}
