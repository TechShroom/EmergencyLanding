package emergencylanding.k.library.debug;

import emergencylanding.k.library.netty.IReceivePacket;
import emergencylanding.k.library.netty.ISendPacket;
import emergencylanding.k.library.netty.NetHandler;
import emergencylanding.k.library.netty.Packet;
import emergencylanding.k.library.util.DataStruct;

public class TestPacket extends Packet implements ISendPacket, IReceivePacket {

    public TestPacket(String msg) {
        this(new DataStruct(new Object[] { msg }));
    }

    public TestPacket(DataStruct dataStruct) {
        super(dataStruct);
    }

    @Override
    public ISendPacket receive(NetHandler n) {
        System.err.println("Recived packet data " + data.get(0));
        return new TestPacket((String) data.get(0));
    }

    @Override
    public boolean send(NetHandler n) {
        System.err.println("Sent packet data " + data.get(0));
        return n.sendPacket(this);
    }

}
