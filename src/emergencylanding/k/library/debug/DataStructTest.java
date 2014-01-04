package emergencylanding.k.library.debug;

import k.core.util.Helper;
import emergencylanding.k.library.util.DataStruct;

public class DataStructTest {
    String aVal = "'look, it worked, alright?' -" + getClass().getSimpleName();

    /**
     * @param args
     */
    public static void main(String[] args) {
        Object[] all = new Object[] { "string", 1, 1.1d, 1.1f, 1l, (byte) 1,
                '|', true, new DataStructTest() };
        Helper.Arrays.print(all);
        // A data struct with all types in one.
        DataStruct dec = new DataStruct(all);
        DataStruct enc = new DataStruct(dec.toString());
        Helper.Arrays.print(enc.getAll());
    }

    @Override
    public String toString() {
        return aVal;
    }

}
