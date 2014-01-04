package emergencylanding.k.library.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TelnetInitServer extends ChannelInitializer<SocketChannel> {
    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();
    private NetHandlerServer nhs = null;

    public TelnetInitServer(NetHandlerServer netHandlerServer) {
        nhs = netHandlerServer;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // Add the text line codec combination first,
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192,
                Delimiters.lineDelimiter()));
        // the encoder and decoder are static as these are sharable
        pipeline.addLast("decoder", DECODER);
        pipeline.addLast("encoder", ENCODER);
        nhs.server = new TelnetServer();
        nhs.server.server = nhs;
        // and then business logic.
        pipeline.addLast("handler", nhs.server);
    }
}