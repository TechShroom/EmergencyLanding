package emergencylanding.k.library.util;

import static org.lwjgl.opengl.GL11.*;

import java.util.LinkedList;

final class GLRotator {
    private static LinkedList<GLRotator> rots = null;
    private double irx, iry, irz, itheta; // inverse values, theta is normal

    private GLRotator() {
    }

    static void glBeginRot(double theta, double rx, double ry, double rz) {
        rots.add(new GLRotator());
        GLRotator rot = rots.peek();
        rot.irx = -rx;
        rot.iry = -ry;
        rot.irz = -rz;
        rot.itheta = theta;
        glRotated(theta, rx, ry, rz);
    }

    static void glEndRot() {
        GLRotator rot = rots.pollLast();
        glRotated(rot.itheta, rot.irx, rot.iry, rot.irz);
    }
}
