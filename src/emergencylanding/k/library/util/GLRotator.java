package emergencylanding.k.library.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

import emergencylanding.k.library.util.Maths.Axis;

final class GLRotator {
    
    private GLRotator() {
    }

    static void glBeginRot(double theta, double rx, double ry, double rz) {
        List<Matrix4f> mats = mats(theta, rx, ry, rz);
        PreShaderOps.add(mats.toArray(new Matrix4f[mats.size()]));
    }

    private static List<Matrix4f> mats(double theta, double rx, double ry,
            double rz) {
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
