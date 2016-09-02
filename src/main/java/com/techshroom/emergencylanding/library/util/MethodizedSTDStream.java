/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <https://techshoom.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.techshroom.emergencylanding.library.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

public class MethodizedSTDStream extends ByteArrayOutputStream {

    PrintStream orig = null;
    String data = "";
    boolean lastNewline = true, autoFlush = false;

    public MethodizedSTDStream(PrintStream out) {
        this.orig = out;
        // Sure, why not.
        this.autoFlush = true;
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        try {
            String str = new String(Arrays.copyOfRange(b, off, len));
            str = replaceAllButLast(str, "\\r?\\n", "$0" + getMethod());
            if (this.lastNewline) {
                this.data += getMethod() + str;
            } else {
                this.data += str;
            }
            this.lastNewline = str.endsWith("\n");
            if (this.autoFlush) {
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
                    && !(s.getClassName().equals(LUtils.class.getName()) && s.getMethodName().equals("print"))) {
                break;
            }
        }
        if (s == null) {
            // there is no stack!
            throw new IllegalStateException("No stack!");
        }
        String[] classsplit = s.getClassName().split("\\.");
        return "[" + classsplit[classsplit.length - 1] + "." + s.getMethodName() + "(" + s.getFileName() + ":"
                + s.getLineNumber() + ")@" + Thread.currentThread().getName() + "] ";
    }

    @Override
    public void flush() throws IOException {
        this.orig.write(this.data.getBytes());
        this.orig.flush();
        this.data = "";
    }

}
