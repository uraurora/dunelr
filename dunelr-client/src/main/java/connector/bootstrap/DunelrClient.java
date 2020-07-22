package connector.bootstrap;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import connector.pipeline.DunelrClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadFactory;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-17 17:50
 * @description : dunelr的客户端
 */
public class DunelrClient {
    private static final Logger logger = LogManager.getLogger(DunelrClient.class);

    private final URI uri;

    private final int port;

    private final ThreadFactory threadFactory;

    private Channel channel;

    private EventLoopGroup group;


    //<editor-fold desc="builder pattern">
    private DunelrClient(DunelrClientBuilder builder) {
        uri = builder.uri;
        port = builder.port;
        threadFactory = builder.threadFactory;
    }

    public static final class DunelrClientBuilder {
        private URI uri;
        private int port;
        private ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Dunelr-netty-client-%d")
                .build();

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

        public DunelrClientBuilder threadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        public DunelrClient build() {
            return new DunelrClient(this);
        }
    }

    public static DunelrClientBuilder builder(){
        return new DunelrClientBuilder();
    }
    //</editor-fold>

    public void start(){
        group = new NioEventLoopGroup(threadFactory);
        try {
            // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
            // If you change it to V00, ping is not supported and remember to change
            // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.

            final WebSocketClientHandshaker handShaker = WebSocketClientHandshakerFactory.newHandshaker(
                    uri, WebSocketVersion.V13,
                    null,
                    true, new DefaultHttpHeaders()
            );

            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new DunelrClientInitializer(handShaker));

            channel = bootstrap.connect(uri.getHost(), port).sync().channel();
            // handler.handshakeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("client error : " + e);
        }
    }

    public static void main(String[] args) {
        DunelrClient client = null;
        try {
            client = DunelrClient.builder()
                    .host(new URI("ws://127.0.0.1:8080/websocket"))
                    .port(8080)
                    .build();
            client.start();
            Channel ch = client.channel;
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String msg = console.readLine();
                if (msg == null) {
                    break;
                } else if ("bye".equals(msg.toLowerCase())) {
                    ch.writeAndFlush(new CloseWebSocketFrame());
                    try {
                        ch.closeFuture().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                } else if ("ping".equals(msg.toLowerCase())) {

                    WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] { 8, 1, 8, 1 }));
                    ch.writeAndFlush(frame);
                } else {
                    WebSocketFrame frame = new TextWebSocketFrame(msg);
                    ch.writeAndFlush(frame);
                }
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }  finally {
            assert client != null;
            client.group.shutdownGracefully();
        }
    }

}
