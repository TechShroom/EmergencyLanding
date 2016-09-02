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

import java.util.function.IntUnaryOperator;

public class Strings {

    public static String repeat(int codePoint, int times) {
        if (times == 0) {
            return "";
        }
        char[] codeChars = Character.toChars(codePoint);
        StringBuilder str = new StringBuilder(codeChars.length * times);
        for (int i = 0; i < times; i++) {
            str.append(codeChars);
        }
        return str.toString();
    }

    public static String repeat(String s, int times) {
        if (s.isEmpty() || times == 0) {
            return "";
        }
        StringBuilder str = new StringBuilder(s.length() * times);
        for (int i = 0; i < times; i++) {
            str.append(s);
        }
        return str.toString();
    }

    /**
     * Returns the amount of <tt>c</tt>'s in <tt>s</tt>
     * 
     * @param s
     *            - the string to check
     * @param c
     *            - the code point to count
     * @return the number of <tt>c</tt>'s in <tt>s</tt>
     */
    public static int count(String s, int c) {
        // int cast ok - all strings < Integer.MAX_VALUE length
        return (int) s.codePoints().filter(x -> x == c).count();
    }

    /**
     * Returns the matching <tt>char</tt> for <tt>number</tt>. Only works with
     * numbers 0-9.
     * 
     * @param number
     *            - a number from 0-9
     * @return the char that matches
     */
    public static char getCharForNum(int number) {
        if (number > 9) {
            throw new IndexOutOfBoundsException("number > 9");
        }
        // the ascii offset for numbers is 48, adding 48 gets the right char
        return (char) (number + 48);
    }

    /**
     * Returns the matching <tt>number</tt> for <tt>c</tt>. Only works with
     * chars '0'-'9'.
     * 
     * @param c
     *            - a char from '0'-'9'
     * @return the char that matches
     */
    public static byte getNumForChar(char c) {
        if (c < 48 || c > 57) {
            throw new IndexOutOfBoundsException("not 0-9");
        }
        // the ascii offset for numbers is 48, subtracting 48 gets the right
        // byte
        return (byte) (c - 48);
    }

    public static String uppercaseFirstCodePoint(String s) {
        return modifyFirstCodePoint(s, Character::toUpperCase);
    }

    public static String lowercaseFirstCodePoint(String s) {
        return modifyFirstCodePoint(s, Character::toLowerCase);
    }

    public static String modifyFirstCodePoint(String s, IntUnaryOperator op) {
        if (s.length() == 0) {
            return s;
        } else if (s.length() == 1) {
            return s.toUpperCase();
        }
        int codePoint = s.codePointAt(0);
        int sizeOfCodePoint = Character.charCount(codePoint);
        char[] mapped = Character.toChars(op.applyAsInt(codePoint));
        return new StringBuilder(s.length()).append(mapped)
                .append(s.substring(sizeOfCodePoint)).toString();
    }

}
