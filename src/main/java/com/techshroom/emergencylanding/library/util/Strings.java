/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <http://techshoom.com>
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

public class Strings {
    /**
     * Returns the amount of <tt>c</tt>'s in <tt>s</tt>
     * 
     * @param s
     *            - the string to check
     * @param c
     *            - the char to count
     * @return the number of <tt>c</tt>'s in <tt>s</tt>
     */
    public static int count(String s, char c) {
        // regex with negated char = removal of all but the char
        return s.replaceAll("[^\\Q" + c + "\\E]", "").length();
    }

    /**
     * Returns the matching <tt>char</tt> for <tt>number</tt>. Only works with
     * numbers 0-9.
     * 
     * @param number
     *            - a number from 0-9
     * @return the char that matches
     */
    public static char getCharForNum(byte number) {
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
}
