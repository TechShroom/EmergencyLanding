package emergencylanding.k.library.debug;

public class Debug {
    private static Object[] useablespace = new Object[1];

    public static void forceMemoryCrash() {
	Debug.useablespace = new int[Integer.MAX_VALUE][Integer.MAX_VALUE];
	Debug.useablespace.hashCode();
	Debug.forceMemoryCrash();// just in case.
    }

    public static void fixForcedMemoryCrash() {
	Debug.useablespace = null;
	System.gc();
    }

}
