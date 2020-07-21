package connector.pipeline;

import connector.handler.DunelrClientHttpResponseHandler;
import connector.handler.DunelrClientWebSocketHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-17 15:17
 * @description : dunelr的客户端管道
 */
public class DunelrClientInitializer extends ChannelInitializer<Channel> {

    private final WebSocketClientHandshaker handShaker;

    // 如果需要广播到其他的客户端，需要引入一个ChannelGroup传给业务Handler

    public DunelrClientInitializer(WebSocketClientHandshaker  handShaker) {
        this.handShaker = handShaker;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
                // Http服务端的编解码器
                new HttpClientCodec(),
                // 传输文件的handler，没有加密和压缩的时候效率更高，防止大文件传输的内存溢出
                new ChunkedWriteHandler(),
                // Http内容聚合handler，经过此处理后变为FullHttp
                new HttpObjectAggregator(64 * 1024),
                // 压缩
                WebSocketClientCompressionHandler.INSTANCE,
                // 处理HttpResponse的业务
                DunelrClientHttpResponseHandler.newInstance(handShaker),

                // websocketHandler通用handler
                //new WebSocketClientProtocolHandler(handShaker),
                // 处理websocket相关业务的handler
                DunelrClientWebSocketHandler.newInstance()

        );
    }
}
