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

import com.flowpowered.math.matrix.Matrix4f;

final class GLScaler {

    private double isx, isy, isz;

    private GLScaler() {
    }

    static void glBeginScale(double sx, double sy, double sz) {
        GLScaler scaler = new GLScaler();
        scaler.isx = sx;
        scaler.isy = sy;
        scaler.isz = sz;
        Matrix4f mats = scaler.mats(false);
        PreShaderOps.add(mats);
    }

    private Matrix4f mats(boolean inv) {
        double sx = this.isx, sy = this.isy, sz = this.isz;
        if (inv) {
            sx = 1 / sx;
            sy = 1 / sy;
            sz = 1 / sz;
        }
        return Maths.createScaleMatrix(sx, sy, sz);
    }

    static void glEndScale() {
        PreShaderOps.remove(1);
    }
}
