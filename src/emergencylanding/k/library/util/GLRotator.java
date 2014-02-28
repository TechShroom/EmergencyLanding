package emergencylanding.k.library.util;

import java.util.*;

import org.lwjgl.util.vector.Matrix4f;

import emergencylanding.k.library.lwjgl.render.GLData;
import emergencylanding.k.library.util.Maths.Axis;

final class GLRotator {
    private static LinkedList<GLRotator> rots = new LinkedList<GLRotator>();
    private double irx, iry, irz; // inverse values
    private double theta; // regular values

    private GLRotator() {
    }

    static void glBeginRot(double theta, double rx, double ry, double rz) {
        GLRotator rot = new GLRotator();
        rot.irx = -rx;
        rot.iry = -ry;
        rot.irz = -rz;
        rot.theta = theta;
        List<Matrix4f> mats = rot.mats(false);
        Matrix4f input = GLData.getMatrixToApply();
        for (Matrix4f m : mats) {
            Matrix4f.mul(input, m, input);
        }
        GLData.apply(input);
        rots.add(rot);
    }

    private List<Matrix4f> mats(boolean inv) {
        double rx = irx, ry = iry, rz = irz;
        if (!inv) {
            rx = -rx;
            ry = -ry;
            rz = -rz;
        }
        rx *= theta;
        ry *= theta;
        rz *= theta;
        List<Matrix4f> out = new ArrayList<Matrix4f>();
        if (rx != 0) {
            out.add(Maths.createRotMatrix(rx, Axis.X));
        }
        if (ry != 0) {
            out.add(Maths.createRotMatrix(ry, Axis.Y));
        }
        if (rz != 0) {
            out.add(Maths.createRotMatrix(rz, Axis.Z));
        }
        return out;
    }

    static void glEndRot() {
        GLRotator rot = rots.pollLast();
        List<Matrix4f> mats = rot.mats(true);
        Matrix4f input = GLData.getMatrixToApply();
        for (Matrix4f m : mats) {
            Matrix4f.mul(input, m, input);
        }
        GLData.apply(input);
    }
}
