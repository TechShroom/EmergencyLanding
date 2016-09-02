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

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class BetterArrays {

    public static void print(Object[] array) {
        print(array, System.err);
    }

    public static void print(Object[] array, PrintStream o) {
        if (array == null) {
            o.println("Array is null.");
        }
        o.println(array.getClass().getComponentType().getName() + "[] (length: "
                + array.length + ") contents:");
        int index = 0;
        for (Object object : array) {
            String out = "";
            String indexs = index + ": ";
            String clazs = "";
            if (object == null) {
                out = "<null object>";
                clazs = array.getClass().getComponentType().getName();
            } else if (object instanceof String
                    && ((String) object).equals("")) {
                out = "<empty string>";
                clazs = object.getClass().getName();
            } else {
                out = object.toString();
                clazs = object.getClass().getName();
            }
            o.println(indexs + "(" + clazs + ") " + out);
            index++;
        }
    }

    public static <T, D> T[] arrayTranslate(Class<T> generic, D[] src) {
        ArrayList<T> temp = new ArrayList<T>(src.length);
        for (D o : src) {
            if (o != null && generic.isAssignableFrom(o.getClass())) {
                temp.add(generic.cast(o));
            } else if (o == null) {
                temp.add((T) null);
            } else {
                System.err.println("Lost " + o + " because it was not of type "
                        + generic.getName());
            }
        }
        // Array is not generic
        @SuppressWarnings("unchecked")
        T[] arrType = (T[]) Array.newInstance(generic, src.length);
        return temp.toArray(arrType);
    }

    public static <T> T[] randomArray(T[] in) {
        if (in.length == 0 || in.length == 1) {
            return in;
        }
        // Array is not generic
        @SuppressWarnings("unchecked")
        T[] test = (T[]) Array.newInstance(in.getClass().getComponentType(),
                in.length);
        boolean solved = false;
        boolean[] taken = new boolean[test.length];
        int total = test.length;
        Random r = new Random(new Random().nextInt());
        int index = 0;
        while (!solved) {
            int ra = r.nextInt(test.length);
            if (!taken[ra]) {
                test[ra] = in[index];
                taken[ra] = true;
                index++;
                total--;
            }
            if (total == 0) {
                solved = true;
            }
        }
        return test;
    }

    public static void dump(Object[] array) {
        for (Object t : array) {
            System.out.println(t);
        }
    }

    public static <T> T[] repeatRandomArray(T[] in, int count) {
        // Array is not generic
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(in.getClass().getComponentType(),
                in.length);
        System.arraycopy(in, 0, array, 0, in.length);
        while (count > -1) {
            array = BetterArrays.randomArray(array);
            count--;
        }
        return array;
    }

    public static String dump0(Object[] array) {
        if (array == null) {
            return "<null array>";
        }
        if (array.length == 0) {
            return "[]";
        }
        String ret = "[";
        for (Object o : array) {
            if (o instanceof String) {
                o = "'" + o + "'";
            }
            ret += o + ", ";
        }
        ret = ret.substring(0, ret.length() - 2) + "]";
        return ret;
    }

    /**
     * I don't know what this does
     * 
     * @param in
     *            - i have no idea
     * @param outtype
     *            - i still have no idea
     * @return - an int array
     */
    public static int[] specificTraslate(byte[] in, int[] outtype) {
        if (in != null && in.length > 0) {
            int[] out =
                    outtype.length >= in.length ? outtype : new int[in.length];
            int index = 0;
            for (byte b : in) {
                out[index] = b;
                index++;
            }
            return out;
        }
        return (int[]) Array.newInstance(outtype.getClass().getComponentType(),
                0);
    }

    public static byte[] intToByteArray(int value) {
        return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
                (byte) (value >>> 8), (byte) value };
    }

    public static <T> T[] reverse(T[] stuff) {
        // Array is not generic
        @SuppressWarnings("unchecked")
        T[] out = (T[]) Array.newInstance(stuff.getClass().getComponentType(),
                stuff.length);
        for (int i = 0; i < stuff.length; i++) {
            out[stuff.length - i - 1] = stuff[i];
        }
        return out;
    }

    /*
     * This is a cool trick which allows us to return the requested array even
     * with primitives as the component type.
     */
    public static <T> T reverseNonGeneric(T t) {
        int tlen = Array.getLength(t);
        // Array is not generic
        @SuppressWarnings("unchecked")
        T out = (T) Array.newInstance(t.getClass().getComponentType(), tlen);
        for (int i = 0; i < tlen; i++) {
            Array.set(out, tlen - i - 1, Array.get(t, i));
        }
        return out;
    }

    public static void fillArray(Object array, Object value) {
        for (int i = 0; i < Array.getLength(array); i++) {
            Array.set(array, i, value);
        }
    }

    public static Object createAndFill(Class<?> type, int size, Object value) {
        Object array = Array.newInstance(type, size);
        fillArray(array, value);
        return array;
    }

    public static <T> T splice(T array, int start, int end, int step) {
        if (start < 0) {
            throw new IndexOutOfBoundsException("start < 0");
        }
        if (end > Array.getLength(array)) {
            throw new IndexOutOfBoundsException("end > length");
        }
        if (end < start) {
            throw new IllegalArgumentException("start < end");
        }
        if (step == 0) {
            throw new IllegalArgumentException("step == 0");
        }
        int len = end - start; // get length when step == 1
        if (step > 1) {
            int mod = len % step; // modulo to get leftovers
            len -= mod; // remove them to floor the result
            len /= step; // divide by step
        }
        // Array is not generic
        @SuppressWarnings("unchecked")
        T out = (T) Array.newInstance(array.getClass().getComponentType(), len);
        if (step < 0) {
            step = -step;
            for (int i = end - 1, index = 0; i >= start; i -= step, index++) {
                Array.set(out, index, Array.get(array, i));
            }
        } else {
            for (int i = start, index = 0; i < end; i += step, index++) {
                Array.set(out, index, Array.get(array, i));
            }
        }
        return out;
    }
}