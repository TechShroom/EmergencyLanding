package emergencylanding.k.library.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Handles packets on the server side
 * 
 * @author Kenzie Togami
 * 
 */
public class NetHandlerServer extends NetHandler {
    ServerBootstrap sb = null;
    ChannelFuture futureNow = null;
    Channel ch = null;
    EventLoopGroup boss = new NioEventLoopGroup(),
            worker = new NioEventLoopGroup();
    TelnetServer server = null;

    public NetHandlerServer(int port) {
        try {
            bootstrap(port);
        } catch (InterruptedException e) {
            e.printStackTrace();
            shutdown();
        }
    }

    private void bootstrap(int port) throws InterruptedException {
        sb = new ServerBootstrap().localAddress(port).group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new TelnetInitServer(this));
        futureNow = sb.bind();
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
        boolean res = server.sendPacket(p);
        server.writePackets();
        return res;
    }

    @Override
    public void shutdown() {
        sync();
        ch.close().syncUninterruptibly();
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }

}
