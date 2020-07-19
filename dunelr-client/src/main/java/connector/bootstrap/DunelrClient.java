package connector.bootstrap;


import connector.handler.DunelrHttpResponseHandler;
import connector.handler.DunelrWebSocketHandler;
import connector.pipeline.DunelrClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-17 17:50
 * @description : dunelr的客户端
 */
public class DunelrClient {
    private final URI uri;

    private final int port;

    private Channel channel;

    //<editor-fold desc="builder pattern">
    private DunelrClient(DunelrClientBuilder builder) {
        uri = builder.uri;
        port = builder.port;
    }

    public static final class DunelrClientBuilder {
        private URI uri;
        private int port;

        private DunelrClientBuilder() {
        }

        public static DunelrClientBuilder aDunelrClient() {
            return new DunelrClientBuilder();
        }

        public DunelrClientBuilder host(URI host) {
            this.uri = host;
            return this;
        }

        public DunelrClientBuilder port(int port) {
            this.port = port;
            return this;
        }

        public DunelrClient build() {
            return new DunelrClient(this);
        }
    }
    //</editor-fold>

    public void start(){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
            // If you change it to V00, ping is not supported and remember to change
            // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
            final DunelrHttpResponseHandler handler = DunelrHttpResponseHandler.newInstance(
                    WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders())
            );

            Bootstrap b = new Bootstrap()
                    .group(group).channel(NioSocketChannel.class)
                    .handler(new DunelrClientInitializer());

            channel = b.connect(uri.getHost(), port).sync().channel();
            handler.handshakeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
