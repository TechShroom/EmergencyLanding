package emergencylanding.k.library.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

public class MethodizedSTDStream extends ByteArrayOutputStream {
    PrintStream orig = null;
    private boolean nextWriteIsOff;
    static String sep = System.getProperty("line.seperator", "\n");

    public MethodizedSTDStream(PrintStream out) {
	orig = out;
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
	byte[] aba = Arrays.copyOfRange(b, off, len);
	if (new String(aba).endsWith(sep)) {
	    orig.println();
	    return;
	}
	StackTraceElement[] ste = new Throwable().getStackTrace();

	// use 1 to skip over our entry, and also keep var after loop
	int i = 1;
	StackTraceElement s = null, sm1;
	for (; i < ste.length; i++) {
	    s = ste[i];
	    if (!s.getClassName().matches("^(java|sun)(.+?)")
		    && !(s.getClassName().equals(LUtils.class.getName()) && s
			    .getMethodName().equals("print"))) {
		break;
	    }
	}
	if (s == null) {
	    // there is no stack!
	    throw new IllegalStateException("No stack!");
	}
	sm1 = ste[i - 1];
	if (sm1.getClassName().equals(PrintStream.class.getName())
		&& sm1.getMethodName().equals("print")) {
	    nextWriteIsOff = true;
	} else if (nextWriteIsOff) {
	    orig.write(b, off, len);
	    nextWriteIsOff = false;
	    return;
	}
	String[] classsplit = s.getClassName().split("\\.");

	String method = "[" + classsplit[classsplit.length - 1] + "."
		+ s.getMethodName() + "()] ";

	orig.write((method + new String(b)).getBytes(), off,
		len + method.getBytes().length);
    }

    @Override
    public void flush() throws IOException {
	orig.flush();
	reset();
    }

}
