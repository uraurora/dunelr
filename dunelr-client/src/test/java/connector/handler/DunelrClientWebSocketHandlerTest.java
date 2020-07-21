package connector.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static org.junit.Assert.*;

public class DunelrClientWebSocketHandlerTest {

    @Test
    public void channelRead0() throws URISyntaxException {

        EmbeddedChannel channel = new EmbeddedChannel(DunelrClientWebSocketHandler.newInstance());
        final TextWebSocketFrame frame = new TextWebSocketFrame("can you hear me?");
        channel.writeInbound(frame.retain());
        assertTrue(channel.finish());
        System.out.println((char[]) channel.readInbound());
    }

}