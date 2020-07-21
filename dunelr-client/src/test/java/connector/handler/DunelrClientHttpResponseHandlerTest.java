package connector.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static org.junit.Assert.*;

public class DunelrClientHttpResponseHandlerTest {

    @Test
    public void channelRead0() throws URISyntaxException {
        final WebSocketClientHandshaker handShaker = WebSocketClientHandshakerFactory.newHandshaker(
                new URI("http://127.0.0.1:8080"), WebSocketVersion.V13,
                null,
                true, new DefaultHttpHeaders()
        );
        EmbeddedChannel channel = new EmbeddedChannel(DunelrClientHttpResponseHandler.newInstance(handShaker));
        final DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, BAD_REQUEST, Unpooled.copiedBuffer("can you hear me?", StandardCharsets.UTF_8));
        assertTrue(channel.writeInbound(response));
        assertTrue(channel.finish());
    }
}