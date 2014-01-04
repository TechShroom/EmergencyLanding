package emergencylanding.k.library.netty;

import java.util.LinkedList;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TelnetServer extends SimpleChannelInboundHandler<String> implements
        PacketSender {
    LinkedList<Packet> send = new LinkedList<Packet>();
    NetHandlerServer server = null;
    public ChannelHandlerContext mostRecent = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        mostRecent = ctx;
        System.err.println("accepted connection on server!");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request)
            throws Exception {
        mostRecent = ctx;
        server.addPacketToRecvQueue(Packet.fromData(request));
        server.processQueue(100);
        writePackets();
    }

    public void writePackets() {
        while (!send.isEmpty()) {
            mostRecent.writeAndFlush(Packet.toData(send.poll()) + "\r\n")
                    .syncUninterruptibly();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        mostRecent = ctx;
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        mostRecent = ctx;
        System.err.println("EXCEPTION: ");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public boolean sendPacket(Packet p) {
        send.addLast(p);
        writePackets();
        return true;
    }
}