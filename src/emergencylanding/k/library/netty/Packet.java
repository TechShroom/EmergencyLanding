package emergencylanding.k.library.netty;

import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import k.core.util.reflect.Reflect;
import emergencylanding.k.library.util.DataStruct;

/**
 * A packet for connections. Implements {@link Comparable} so that the most
 * important packets go through first.
 * 
 * @author Kenzie Togami
 * 
 */
public abstract class Packet implements Comparable<Packet> {

    private static HashMap<Class<? extends Packet>, Integer> packetToId = new HashMap<Class<? extends Packet>, Integer>();
    private static HashMap<Integer, Class<? extends Packet>> idToPacket = new HashMap<Integer, Class<? extends Packet>>();

    public static final void registerPacket(Class<? extends Packet> type, int id) {
        packetToId.put(type, id);
        idToPacket.put(id, type);
    }

    /**
     * Uses an empty constructor for a packet.
     * 
     * @param id
     *            - the id of the packet
     * @return
     */
    public static Packet newPacket(int id) {
        return newPacket(id, new Object[0]);
    }

    /**
     * Uses a constructor that takes a {@link DataStruct} with the given data.
     * 
     * @param id
     *            - the id of the packet
     * @param data
     *            - the data for the packet
     * @return
     */
    public static Packet newPacket(int id, DataStruct data) {
        return newPacket(id, new Object[] { data });
    }

    /**
     * Uses an empty constructor for a packet.
     * 
     * @param id
     *            - the id of the packet
     * @param constrClasses
     *            - find a constructor using these classes
     * @param constrObjects
     *            - use these objects during construction
     * @return
     */
    public static Packet newPacket(int id, Object[] constrObjects) {
        Class<? extends Packet> pClass = idToPacket.get(id);
        if (pClass == null) {
            throw new IllegalStateException("Packet not registered.");
        } else {
            try {
                pClass.getDeclaredConstructor(new Class[] { DataStruct.class });
            } catch (Exception e) {
                throw new RuntimeException(new IllegalClassFormatException(
                        "Packet requires a DataStruct constructor"));
            }
            try {
                return Reflect.construct(pClass, constrObjects);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected DataStruct data = new DataStruct(new Object[0]);

    /**
     * Do not attempt construction of packets. Use {@link Packet#newPacket(int)}
     * , {@link Packet#newPacket(int, DataStruct)}, or
     * {@link Packet#newPacket(int, Class[], Object[])}
     */
    protected Packet(DataStruct ds) {
        data = ds;
    }

    /**
     * Our method for compareTo involves using the registered id for the
     * packets. A lower id = higher priority.
     * 
     * This is <i>inconsistent</i> with equals, because this is used for
     * comparing priority, not equality.
     * 
     * @see Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(Packet o) {
        if (o == null) {
            throw new NullPointerException(
                    "implementation detail for compareTo: null");
        }
        return this.delCompareTo(o);
    }

    /**
     * Delegated method, o is guaranteed to be non-null.
     * 
     * @param o
     *            - the packet to compare to
     * @return see {@link Comparable#compareTo(Object)}
     * @see Comparable#compareTo(Object)
     */
    protected int delCompareTo(Packet o) {
        int ourId = packetToId.get(getClass()), theirId = packetToId.get(o
                .getClass());
        if (ourId == theirId) {
            return 0;
        } else if (ourId < theirId) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "Packet#" + packetToId.get(getClass()) + "[" + data.toString()
                + "]";
    }

    public static Packet fromData(String request) {
        DataStruct ds = new DataStruct(request);
        return newPacket((Integer) ds.get(0),
                new DataStruct((String) ds.get(1)));
    }

    public static String toData(Packet p) {
        String req = new DataStruct(new Object[] {
                packetToId.get(p.getClass()), p.data.toString() }).toString();
        return req;
    }
}
