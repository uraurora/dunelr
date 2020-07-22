package connector.handler;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.junit.Test;

import java.net.URISyntaxException;
import static org.junit.Assert.*;

public class DunelrClientWebSocketHandlerTest {

    @Test
    public void channelRead0() throws URISyntaxException {

        EmbeddedChannel channel = new EmbeddedChannel(DunelrClientWebSocketHandler.newInstance());
        TextWebSocketFrame frame = new TextWebSocketFrame("can you hear me?");
        assertTrue(channel.writeInbound(frame.retain()));
        assertTrue(channel.finish());
        System.out.println((char[]) channel.readInbound());
    }

}