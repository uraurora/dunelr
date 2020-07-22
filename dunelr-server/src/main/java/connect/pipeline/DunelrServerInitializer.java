package connect.pipeline;

import connect.handler.DunelrServerHttpRequestHandler;
import connect.handler.DunelrServerWebSocketHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
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
public class DunelrServerInitializer extends ChannelInitializer<SocketChannel> {
    // 如果需要广播到其他的客户端，需要引入一个ChannelGroup传给业务Handler

    private static final String WEBSOCKET_PATH = "/websocket";

    public DunelrServerInitializer() {
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        // Http服务端的编解码器
        pipeline.addLast("http-server-codec", new HttpServerCodec());
        // 向客户端传输文件的handler，没有加密和压缩的时候效率更高，防止大文件传输的内存溢出
        pipeline.addLast("chunk-writer", new ChunkedWriteHandler());
        // Http内容聚合handler，经过此处理后变为FullHttp
        pipeline.addLast("aggregator", new HttpObjectAggregator(64 * 1024));
        // 压缩
        pipeline.addLast("compression", new WebSocketServerCompressionHandler());
        // 处理HttpRequest的业务
        //pipeline.addLast("http-server-handler", DunelrServerHttpRequestHandler.newInstance());
        // 运行WebSocket服务器大部分工作的handler
        pipeline.addLast("websocket-general-handler", new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        //  处理webSocket相关业务的handler
        pipeline.addLast("websocket-handler", DunelrServerWebSocketHandler.newInstance());

    }
}
