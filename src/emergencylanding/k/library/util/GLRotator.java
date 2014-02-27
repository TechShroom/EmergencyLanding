package emergencylanding.k.library.util;

import static org.lwjgl.opengl.GL11.*;

public final class GLRotator {
    private static final double[] EMPTY = new double[4];
    private double[] rotReset = EMPTY;

    GLRotator() {
    }

    public GLRotator glBeginRot(double theta, double rx, double ry, double rz) {
        rotReset = new double[] { theta + rotReset[0], -rx + rotReset[1],
                -ry + rotReset[2], -rz + rotReset[3]};
        glRotated(theta, rx, ry, rz);
        return this;
    }
    
    public GLRotator glEndRot() {
        glRotated(rotReset[0], rotReset[1], rotReset[2], rotReset[3]);
        rotReset = EMPTY;
        return this;
    }
}
