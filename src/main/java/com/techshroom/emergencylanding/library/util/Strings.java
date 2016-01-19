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
