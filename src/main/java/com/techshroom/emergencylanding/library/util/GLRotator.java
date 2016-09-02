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

import java.util.ArrayList;
import java.util.List;

import com.flowpowered.math.matrix.Matrix4f;
import com.techshroom.emergencylanding.library.util.Maths.Axis;

final class GLRotator {

    private GLRotator() {
    }

    static void glBeginRot(double theta, double rx, double ry, double rz) {
        List<Matrix4f> mats = mats(theta, rx, ry, rz);
        PreShaderOps.add(mats.toArray(new Matrix4f[mats.size()]));
    }

    private static List<Matrix4f> mats(double theta, double rx, double ry, double rz) {
        rx *= theta;
        ry *= theta;
        rz *= theta;
        List<Matrix4f> out = new ArrayList<Matrix4f>();
        out.add(Maths.createRotMatrix(rx, Axis.X));
        out.add(Maths.createRotMatrix(ry, Axis.Y));
        out.add(Maths.createRotMatrix(rz, Axis.Z));
        return out;
    }

    static void glEndRot() {
        PreShaderOps.remove(3);
    }
}
