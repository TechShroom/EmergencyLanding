package emergencylanding.k.library.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import k.core.util.reflect.Reflect;

public class MethodizedSTDStream extends ByteArrayOutputStream {
    PrintStream orig = null;
    String data = "";
    boolean lastNewline = true, autoFlush = false;

    public MethodizedSTDStream(PrintStream out) {
        orig = out;
        try {
            autoFlush = Reflect.getField(Boolean.class, "autoFlush", out);
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
