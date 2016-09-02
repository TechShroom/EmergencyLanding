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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

import com.flowpowered.math.matrix.Matrix4f;
import com.flowpowered.math.vector.Vector3f;

public final class PreShaderOps {

    private static final LinkedList<Matrix4f> applies = new LinkedList<Matrix4f>();
    private static Matrix4f currentProduct;

    private PreShaderOps() {
        throw new AssertionError("Nope.");
    }

    public static void add(Matrix4f... in) {
        for (Matrix4f m : in) {
            applies.add(m);
        }
        if (in.length != 0) {
            currentProduct = null;
        }
    }

    public static void remove(int count) {
        if (count > 0) {
            currentProduct = null;
        }
        for (; count > 0; count--) {
            applies.removeLast();
        }
    }

    private static Matrix4f getApplies() {
        if (currentProduct == null) {
            return currentProduct = applyMatricies();
        }
        return currentProduct;
    }

    private static Matrix4f applyMatricies() {
        LinkedList<Matrix4f> c = applies;
        Matrix4f m;
        if (c.size() >= 1) {
            Iterator<Matrix4f> iter = c.iterator();
            m = iter.next();
            while (iter.hasNext()) {
                Matrix4f mf = iter.next();
                if (mf == null) {
                    System.err.println("Warning: Null matrix");
                    continue;
                }
                m = m.mul(mf);
            }
        } else {
            m = Matrix4f.IDENTITY;
        }
        return m;
    }

    public static Vector3f apply(Vector3f in) {
        return apply(in, getApplies());
    }

    public static Vector3f[] applyMany(Vector3f... in) {
        if (in.length == 1) {
            throw new UnsupportedOperationException("Use apply(Vector3f) for singular array.");
        }
        Matrix4f apply = getApplies();
        return Stream.of(in).map(v -> apply(v, apply)).toArray(Vector3f[]::new);
    }

    /**
     * Applies the matrix to the vector in place.
     * 
     * @param vec
     *            - vector
     * @param m
     *            - matrix to multiply vector by
     */
    private static Vector3f apply(Vector3f vec, Matrix4f m) {
        float vx = vec.getX(), vy = vec.getY(), vz = vec.getZ();
        return new Vector3f(m.get(0, 0) * vx + m.get(0, 1) * vy + m.get(0, 2) * vz + m.get(0, 3),
                m.get(1, 0) * vx + m.get(1, 1) * vy + m.get(1, 2) * vz + m.get(1, 3),
                m.get(2, 0) * vx + m.get(2, 1) * vy + m.get(2, 2) * vz + m.get(2, 3));
    }
}
