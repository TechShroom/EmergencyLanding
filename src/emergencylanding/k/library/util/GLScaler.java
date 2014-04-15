package emergencylanding.k.library.util;

import org.lwjgl.util.vector.Matrix4f;

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
        double sx = isx, sy = isy, sz = isz;
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
