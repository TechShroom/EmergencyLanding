package emergencylanding.k.library.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import k.core.reflect.Reflect;

public class MethodizedSTDStream extends ByteArrayOutputStream {
    PrintStream orig = null;
    String data = "";
    boolean lastNewline = true, autoFlush = false;
    static OutputStream output = null;
    static PrintStream outputps = null;
    static {
	try {
	    output = new FileOutputStream("mstds.txt");
	    outputps = new PrintStream(output, true);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }

    public MethodizedSTDStream(PrintStream out) {
	orig = out;
	try {
	    autoFlush = Reflect.getField(Boolean.class, "autoFlush", out);
	    outputps.println(autoFlush);
	} catch (NoSuchFieldException e) {
	    e.printStackTrace();
	} catch (SecurityException e) {
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * Me figuring out how to do this.
     * 
     * write() is a actually a string, so convert the proper bytes to a string.
     * Take that string, and if this is the first string or the last message was
     * a new line, put the method descriptor data in front.
     * 
     * Other things to consider: Newlines at end of string, eg. "newline here\n"
     * Newlines in string, eg. "newline here\nbut there is more"
     * 
     * Possibly do a recursive method with newlines, eg.
     * "newline here\nwith more" -> "newline here", "\n", "with more"
     */
    @Override
    public synchronized void write(byte[] b, int off, int len) {
	try {
	    String str = new String(Arrays.copyOfRange(b, off, len));
	    str = replaceAllButLast(str, "\\r?\\n", "$0" + getMethod());
	    if (lastNewline) {
		data += getMethod() + str;
	    } else {
		data += str;
	    }
	    lastNewline = str.endsWith("\n");
	    if (autoFlush) {
		flush();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private String replaceAllButLast(String str, String check, String replace) {
	String out = "";
	String sub = str.replaceAll(check + "$", "");
	out = str.replace(sub, sub.replaceAll(check, replace));
	return out;
    }

    private String getMethod() {

	StackTraceElement[] ste = new Throwable().getStackTrace();

	int i = StackTraceInfo.DUAL_INVOKING_METHOD_ZERO;
	StackTraceElement s = null;
	for (; i < ste.length; i++) {
	    s = ste[i];
	    // skip LUtils.print() because we want the method that called that
	    // one.
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
	String[] classsplit = s.getClassName().split("\\.");
	return "[" + classsplit[classsplit.length - 1] + "."
		+ s.getMethodName() + "()@" + Thread.currentThread().getName()
		+ "] ";
    }

    @Override
    public void flush() throws IOException {
	orig.write(data.getBytes());
	orig.flush();
	data = "";
    }

}
