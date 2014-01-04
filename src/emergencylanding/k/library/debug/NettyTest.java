package emergencylanding.k.library.debug;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.main.KMain;
import emergencylanding.k.library.netty.NetHandlerClient;
import emergencylanding.k.library.netty.NetHandlerServer;
import emergencylanding.k.library.netty.Packet;
import emergencylanding.k.library.util.LUtils;

public class NettyTest extends KMain {

    @Override
    public void onDisplayUpdate(int delta) {
    }

    @Override
    public void init(String[] args) {

        // choose server port = 25566
        NetHandlerServer nhs = new NetHandlerServer(25566);
        NetHandlerClient nhc;
        try {
            nhc = new NetHandlerClient(new InetSocketAddress(0),
                    new InetSocketAddress(InetAddress.getLocalHost(), 25566));
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
            nhs.shutdown();
            return;
        }
        Packet.registerPacket(TestPacket.class, 1);
        nhc.addPacketToSendQueue(Packet.newPacket(1, new Object[] { "a msg" }));
        for (int i = 0; i < 100; i++) {
            nhc.processQueue();
            nhs.processQueue();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
        nhc.shutdown();
        nhs.shutdown();
        System.err.println(nhc.sentPackets + ":" + nhs.sentPackets);
    }

    @Override
    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        LUtils.init();
        new NettyTest().init(args);
    }

}
