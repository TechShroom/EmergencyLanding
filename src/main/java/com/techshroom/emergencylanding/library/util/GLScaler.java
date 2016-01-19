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
