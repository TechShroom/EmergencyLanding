package emergencylanding.k.library.util;

import k.core.util.core.Helper.BetterArrays;

import org.lwjgl.util.vector.Matrix4f;

public final class Maths {

    private Maths() {
        throw new AssertionError("Don't create");
    }

    /**
     * Finds the length of a line when projected along another line. Used for
     * collisions.
     * 
     * @param thetaSurface
     *            - The angle the surface to be projected upon makes with the
     *            horizontal
     * @param thetaLineToProject
     *            - The angle the line makes with the horizontal
     * @param magnitude
     *            - The length of the line.
     * @param perpendicular
     *            - Gets the length of the line when projected along the line
     *            perpendicular to the thetaSurface given (eg, find the y).
     */
    public static double projectLineAlongSurface(double thetaSurface,
            double thetaLineToProject, double magnitude, boolean perpendicular) {
        double theta = thetaLineToProject - thetaSurface;
        if (perpendicular) {
            theta += 90;
        }
        return qcos(theta) * magnitude;
    }

    /**
     * Perform linear interpolation on positions
     * 
     * @param pos1
     *            -The first position (does not matter x or y)
     * @param pos2
     *            -The second position
     * @param v
     *            -The interpolaty-bit. Decimal between 0 and 1.
     * @return -The position (actually an average of pos1/pos2 + pos1).
     */
    public static float lerp(float pos1, float pos2, float v) {
        return pos1 + (pos2 - pos1) * v;
    }

    public static enum Axis {
        X, Y, Z
    }

    public static Matrix4f createRotMatrix(double theta, Axis axis) {
        Matrix4f rmat = new Matrix4f(); // identity
        float c = (float) qcos(theta);
        float s = (float) qsin(theta);
        switch (axis) {
        case X:
            rmat.m11 = c;
            rmat.m22 = c;
            rmat.m12 = -s;
            rmat.m21 = s;
            break;
        case Y:
            rmat.m00 = c;
            rmat.m22 = c;
            rmat.m20 = -s;
            rmat.m02 = s;
            break;
        case Z:
            rmat.m00 = c;
            rmat.m11 = c;
            rmat.m01 = -s;
            rmat.m10 = s;
            break;
        default:
            throw new IllegalArgumentException("Invalid Axis");
        }
        return rmat;
    }

    public static Matrix4f createTransMatrix(double ix, double iy, double iz) {
        Matrix4f imat = new Matrix4f(); // identity
        imat.m30 = (float) ix;
        imat.m31 = (float) iy;
        imat.m32 = (float) iz;
        return imat;
    }

    public static Matrix4f createScaleMatrix(double sx, double sy, double sz) {
        Matrix4f imat = new Matrix4f(); // identity
        imat.m00 = (float) sx;
        imat.m11 = (float) sy;
        imat.m22 = (float) sz;
        return imat;
    }

    /* "quick" trig: shortcuts default values for accuracy and speed */
    /*
     * Worse under non-default values due to lookup taking time, but is small
     * enough to be negligible
     */

    private static double[] sin = (double[]) BetterArrays.createAndFill(
            double.class, 361, -5);
    private static double[] cos = (double[]) BetterArrays.createAndFill(
            double.class, 361, -5);
    private static double[] tan = (double[]) BetterArrays.createAndFill(
            double.class, 361, -5);
    private static int zero = 0, thirty = 30, fortyfive = 45, sixty = 60,
            ninety = 90, oneeighty = 180, twoseventy = 270;
    static {
        Double negone = -1d, one = 1d, zerod = 0d;
        double SQRT3 = Math.sqrt(3), SQRT2 = Math.sqrt(2);
        // sin predetermined vals
        sin[zero] = zerod;
        sin[thirty] = 1d / 2;
        sin[fortyfive] = 1d / SQRT2;
        sin[sixty] = SQRT3 / 2;
        sin[ninety] = one;
        sin[oneeighty] = zerod;
        sin[twoseventy] = negone;
        // cos predetermined vals
        cos[zero] = 1d;
        cos[thirty] = SQRT3 / 2;
        cos[fortyfive] = 1d / SQRT2;
        cos[sixty] = 1d / 2;
        cos[ninety] = zerod;
        cos[oneeighty] = negone;
        cos[twoseventy] = zerod;
        // tan predetermined vals
        tan[zero] = zerod;
        tan[thirty] = 1 / SQRT3;
        tan[fortyfive] = one;
        tan[sixty] = SQRT3;
        tan[oneeighty] = zerod;
    }

    public static double qcos(double theta) {
        theta = normalizeDeg(theta);
        return (((int) theta) == theta && cos[(int) theta] != -5) ? cos[(int) theta]
                : Math.cos(Math.toRadians(theta));
    }

    public static double qsin(double theta) {
        theta = normalizeDeg(theta);
        return (((int) theta) == theta && sin[(int) theta] != -5) ? sin[(int) theta]
                : Math.sin(Math.toRadians(theta));
    }

    public static double qtan(double theta) {
        theta = normalizeDeg(theta);
        return (((int) theta) == theta && tan[(int) theta] != -5) ? tan[(int) theta]
                : Math.tan(Math.toRadians(theta));
    }

    public static double normalizeDeg(double theta) {
        while (theta < 0) {
            theta += 360;
        }
        while (theta >= 360) {
            theta -= 360;
        }
        return theta;
    }

}
