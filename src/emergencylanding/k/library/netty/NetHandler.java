package emergencylanding.k.library.netty;

import java.util.PriorityQueue;

import emergencylanding.k.library.debug.FPS;

/**
 * A class that provides abstraction over Netty and is the base for handling
 * packets
 * 
 * @author Kenzie Togami
 * 
 */
public abstract class NetHandler implements PacketSender {
    private static int timerId = FPS.genIndex();
    private PriorityQueue<ISendPacket> pq = new PriorityQueue<ISendPacket>();
    private PriorityQueue<IReceivePacket> pqin = new PriorityQueue<IReceivePacket>();
    public int sentPackets = 0;

    /**
     * Sets up the basics.
     */
    public NetHandler() {

    }

    /**
     * Adds a packet to the send queue. The given {@link Packet} will be casted.
     * 
     * @param p
     *            - an {@link ISendPacket}
     */
    public void addPacketToSendQueue(Packet p) {
        pq.add((ISendPacket) p);
    }

    /**
     * Adds a packet to the receive queue. The given {@link Packet} will be
     * casted.
     * 
     * @param p
     *            - an {@link IReceivePacket}
     */
    public void addPacketToRecvQueue(Packet p) {
        pqin.add((IReceivePacket) p);
    }

    /**
     * Pops one packet off and sends it.
     * 
     * @return the result of calling {@link ISendPacket#send(NetHandler)} on the
     *         packet, or false if there are no packets left
     */
    public boolean popOneSendQueueObject() {
        synchronized (pq) {
            if (pq.isEmpty()) {
                return false;
            }
            return pq.poll().send(this);
        }
    }

    /**
     * Pops one packet off and receives it.
     * 
     * @return the result of calling {@link IReceivePacket#receive(NetHandler)}
     *         on the packet, or null if there are no packets left
     */
    public ISendPacket popOneReceiveQueueObject() {
        synchronized (pqin) {
            if (pqin.isEmpty()) {
                return null;
            }
            return pqin.poll().receive(this);
        }
    }

    /**
     * Processes the queue until empty.
     */
    public void processQueue() {
        processQueue(0);
    }

    /**
     * Processes the queue until empty or the given timeout.
     * 
     * @param timeout
     *            - a timeout, in milliseconds. If timeout <= 0, it will be
     *            until {@link Long#MAX_VALUE}
     */
    public void processQueue(long timeout) {
        FPS.init(timerId, FPS.millis);
        long count = 0;
        if (timeout <= 0) {
            timeout = Long.MAX_VALUE;
        }
        while (!pqin.isEmpty() && count < timeout) {
            IReceivePacket before = pqin.peek();
            ISendPacket sp = popOneReceiveQueueObject();
            if (sp == null) {
                System.err.println("Couldn't receive packet: " + before);
            } else {
                pq.add(sp);
            }
            count += FPS.getDelta(timerId);
        }
        if (count >= timeout) {
            System.err.println("Didn't get to process " + pqin.size()
                    + " input packets! Timeout was " + count + "ms.");
        }
        count = 0;
        FPS.init(timerId);
        while (!pq.isEmpty() && count < timeout) {
            ISendPacket before = pq.peek();
            if (!popOneSendQueueObject()) {
                System.err.println("Couldn't send packet: " + before);
            } else {
                sentPackets++;
            }
            count += FPS.getDelta(timerId);
        }
        if (count >= timeout) {
            System.err.println("Didn't get to process " + pq.size()
                    + " output packets! Timeout was " + count + "ms.");
        }
    }

    /**
     * Shuts down this handler.
     */
    public abstract void shutdown();
}
