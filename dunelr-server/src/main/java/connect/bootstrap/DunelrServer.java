package connect.bootstrap;

import connect.api.IDunelrServer;
import connect.pipeline.DunelrServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-30 14:01
 * @description :
 */
public class DunelrServer implements IDunelrServer {
    /**
     * 服务端监听的端口
     */
    private final int port;
    /**
     * 线程工厂
     */
    private final ThreadFactory threadFactory;
    /**
     * 启动服务端的channel
     */
    private Channel channel;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    //<editor-fold desc="builder pattern">
    /**
     * 构造函数
     * @param builder 建造者模式
     */
    private DunelrServer(DunelrServerBuilder builder) {
        port = builder.port;
        threadFactory = builder.threadFactory;
    }

    public static final class DunelrServerBuilder {
        private int port;
        private ThreadFactory threadFactory;

        private DunelrServerBuilder() {
        }

        public static DunelrServerBuilder aDunelrServer() {
            return new DunelrServerBuilder();
        }

        public DunelrServerBuilder port(int port) {
            this.port = port;
            return this;
        }

        public DunelrServerBuilder threadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        public DunelrServer build() {
            return new DunelrServer(this);
        }
    }
    //</editor-fold>

    @Override
    public void start() {
        init();
        Runtime.getRuntime().addShutdownHook(threadFactory.newThread(this::stop));
        channel.closeFuture().syncUninterruptibly();
    }

    @Override
    public void stop(){
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    private void init(){
        bossGroup = new NioEventLoopGroup(1, threadFactory);
        workerGroup = new NioEventLoopGroup(threadFactory);

        ServerBootstrap b = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new DunelrServerInitializer());

        ChannelFuture future = b.bind(new InetSocketAddress(port));
        future.syncUninterruptibly();
        channel = future.channel();
    }


}
