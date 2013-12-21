package emergencylanding.k.library.debug;

import emergencylanding.k.library.util.DataStruct;

public class DataStructTest {

    /**
     * @param args
     */
    public static void main(String[] args) {

	// A data struct with all thpes in one.
	DataStruct dec = new DataStruct(new Object[] { "string", 1, 1.1d, 1.1f,
		1l, (byte) 1, (char) 1, true, new DataStructTest() });
	DataStruct enc = new DataStruct(dec.toString());
    }

}
