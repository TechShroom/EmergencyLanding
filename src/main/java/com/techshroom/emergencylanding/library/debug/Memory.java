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
package com.techshroom.emergencylanding.library.debug;

import com.techshroom.emergencylanding.library.util.BetterArrays;

public class Memory {

    private static long lastF = Memory.getFree();
    private static long lastM = Memory.getMax();
    private static long lastT = Memory.getTotal();

    private Memory() {
        throw new AssertionError();
    }

    public static void printFree() {
        long free = Memory.getFree();
        System.out.println("FREE_MEM: " + free);
        Memory.lastF = free;
    }

    public static void printMax() {
        long max = Memory.getMax();
        System.out.println("MAX_MEM: " + max);
        Memory.lastM = max;
    }

    public static void printTotal() {
        long total = Memory.getTotal();
        System.out.println("TOTAL_MEM: " + total);
        Memory.lastT = total;
    }

    public static long getFree() {
        return Runtime.getRuntime().freeMemory();
    }

    public static long getMax() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long getTotal() {
        return Runtime.getRuntime().totalMemory();
    }

    public static void printAll() {
        Memory.printFree();
        Memory.printMax();
        Memory.printTotal();
    }

    public static void comparePrint() {
        String[] lines = new String[3];
        if (Memory.getFree() < Memory.lastF) {
            lines[0] = "Free memory is less.";
        } else {
            lines[0] = "Free memory is more!";
        }

        if (Memory.getMax() < Memory.lastM) {
            lines[1] = "Max memory is less.";
        } else {
            lines[1] = "Max memory is more!";
        }

        if (Memory.getTotal() < Memory.lastT) {
            lines[2] = "Total memory is less.";
        } else {
            lines[2] = "Total memory is more!";
        }
        BetterArrays.dump(lines);
    }

    public static void gc() {
        System.gc();
    }

}
