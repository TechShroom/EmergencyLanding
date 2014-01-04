package emergencylanding.k.library.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;

/**
 * Handles packets on the client side.
 * 
 * @author Kenzie Togami
 * 
 */
public class NetHandlerClient extends NetHandler {
    Bootstrap b = null;
    ChannelFuture futureNow = null;
    Channel ch = null;
    EventLoopGroup group = new NioEventLoopGroup();
    TelnetClient client = null;

    public NetHandlerClient(SocketAddress from, SocketAddress to) {
        try {
            bootstrap(from, to);
        } catch (InterruptedException e) {
            e.printStackTrace();
            shutdown();
        }
    }

    private void bootstrap(SocketAddress from, SocketAddress to)
            throws InterruptedException {
        b = new Bootstrap().localAddress(from).remoteAddress(to).group(group)
                .channel(NioSocketChannel.class)
                .handler(new TelnetInitClient(this));
        futureNow = b.connect();
        // separate the calls so that other threads have a chance to cancel on
        // futureNow.
        ch = futureNow.channel();
        sync();
    }

    private void sync() {
        if (futureNow == null) {
            return;
        }
        futureNow.syncUninterruptibly();
        futureNow = null;
    }

    @Override
    public boolean sendPacket(Packet p) {
        boolean res = client.sendPacket(p);
        client.writePackets();
        return res;
    }

    @Override
    public void shutdown() {
        sync();
        ch.close().syncUninterruptibly();
        group.shutdownGracefully();
    }

}
