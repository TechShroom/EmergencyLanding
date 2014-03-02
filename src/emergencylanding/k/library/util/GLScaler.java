package emergencylanding.k.library.util;

import java.util.LinkedList;

import org.lwjgl.util.vector.Matrix4f;

import emergencylanding.k.library.lwjgl.render.GLData;

final class GLScaler {
    private static LinkedList<GLScaler> scalers = new LinkedList<GLScaler>();
    private double isx, isy, isz; // inverse values

    private GLScaler() {
    }

    static void glBeginScale(double sx, double sy, double sz) {
        GLScaler scaler = new GLScaler();
        scaler.isx = 1/sx;
        scaler.isy = 1/sy;
        scaler.isz = 1/sz;
        Matrix4f mats = scaler.mats(false);
        Matrix4f input = GLData.getMatrixToApply();
        Matrix4f.mul(input, mats, input);
        GLData.apply(input);
        scalers.add(scaler);
    }

    private Matrix4f mats(boolean inv) {
        double sx = isx, sy = isy, sz = isz;
        if (!inv) {
            sx = 1/sx;
            sy = 1/sy;
            sz = 1/sz;
        }
        return Maths.createScaleMatrix(sx, sy, sz);
    }

    static void glEndScale() {
        GLScaler scaler = scalers.pollLast();
        Matrix4f mats = scaler.mats(true);
        Matrix4f input = GLData.getMatrixToApply();
        Matrix4f.mul(input, mats, input);
        GLData.apply(input);
        GLData.apply(input);
    }
}
