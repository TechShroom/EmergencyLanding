package emergencylanding.k.library.netty;

import emergencylanding.k.library.util.DataStruct;

public interface PacketSender {

    /**
     * Sends the packets {@link DataStruct}.
     * 
     * @param p
     *            - the packet to send
     * @return if the packet was sent.
     */
    public abstract boolean sendPacket(Packet p);
}
