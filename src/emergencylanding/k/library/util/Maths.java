package emergencylanding.k.library.util;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import k.core.util.core.Helper.BetterArrays;

import org.lwjgl.util.vector.Matrix4f;

public final class Maths {

    public static final double med = calculateMachineEpsilonDouble();

    static {
        System.err.println("Got MED " + med);
    }

    private static double calculateMachineEpsilonDouble() {
        double machEps = 1.0d;

        do
            machEps /= 2.0d;
        while ((double) (1.0 + (machEps / 2.0)) != 1.0);

        return machEps;
    }

    /**
     * Provides geometry manipulation, such as axis-normalization and rectangle
     * rotations.
     * 
     * @author Kenzie Togami
     */
    public static final class Geometry {
        private Geometry() {
            throw new AssertionError("Don't create");
        }

        /**
         * Rotates the given rectangle by <code>theta</code> degrees, treating
         * it as if it were drawn at that angle and not axis-aligned.
         * 
         * @param r
         *            - the original rectangle
         * @param theta
         *            - the rotation angle
         * @return <code>r</code>, rotated by <code>theta</code>
         */
        public static Rectangle2D rotateRect(Rectangle2D r, double theta) {
            // store data
            double x = r.getX(), y = r.getY(), w = r.getWidth(), h = r
                    .getHeight();
            // clear rect
            r.setRect(0, 0, 0, 0);
            // get points
            Point2D[] points = new Point2D[] { new Point2D.Double(x, y),
                    new Point2D.Double(w + x, h + y) };
            // calc cos/sin
            double s = qsin(theta), c = qcos(theta);
            // expand rect to fit
            for (Point2D p : points) {
                p.setLocation((p.getX() * c) - (p.getY() * s), (p.getX() * s)
                        + (p.getY() * c));
            }
            r.setRect(points[0].getX(), points[0].getY(), points[1].getX()
                    - points[0].getX(), points[1].getY() - points[0].getY());
            return r;
        }

        public static double[] pointsAsDoubles(Point2D[] points) {
            double[] out = new double[points.length * 2];
            for (int i = 0; i < out.length; i += 2) {
                Point2D p = points[i / 2];
                out[i] = p.getX();
                out[i + 1] = p.getY();
            }
            return out;
        }

        public static Point2D[] doublesAsPoints(double[] points) {
            if (points.length % 2 != 0) {
                throw new IllegalArgumentException("need pairs of doubles");
            }
            Point2D[] temp = new Point2D[points.length / 2];
            for (int i = 0; i < points.length; i += 2) {
                temp[i / 2] = new Point2D.Double(points[i], points[i + 1]);
            }
            return temp;
        }
    }

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
     * @param getY
     *            - Gets the length of the line when projected along the line
     *            perpendicular to the thetaSurface given (eg, find the y).
     */
    public static double projectLineAlongSurface(double thetaSurface,
            double thetaLineToProject, double magnitude, boolean getY) {
        double dp = dotProductAngles(magnitude, thetaLineToProject, 1, thetaSurface);
        if(!getY)
        {
            System.out.println(dp);
            return dp*Math.cos(Math.toRadians(thetaSurface));
        }else
        {
            System.out.println(dp);
            return dp*Math.sin(Math.toRadians(thetaSurface));
        }
        
    }
    
    public static double projectLineAlongSurfaceXY(double xSurface,
            double ySurface, double xLine, double yLine, boolean DoY) {
        double dp = dotProduct(xLine, yLine, Maths.normalizeX(xSurface, ySurface), Maths.normalizeY(xSurface, ySurface));
        if(!DoY)
        {
            System.out.println(dp);
            return dp*xSurface;
        }else
        {
            System.out.println(dp);
            return dp*ySurface;
        }
        
    }

    public static double normalizeX(double x, double y)
    {
        double len_v = Math.sqrt(x*x + y*y);
        return x / len_v;
    }
    
    public static double normalizeY(double x, double y)
    {
        double len_v = Math.sqrt(x*x + y*y);
        return y / len_v;
    }
    
    public static double dotProduct(double ax, double ay, double bx, double by)
    {
        return ax*ay + bx*by;
    }
    
    public static double dotProductAngles(double amag, double atheta, double bmag, double btheta)
    {
        double ax = Math.cos(Math.toRadians(atheta))*amag;
        double bx = Math.cos(Math.toRadians(btheta))*bmag;
        double ay = Math.sin(Math.toRadians(atheta))*amag;
        double by = Math.sin(Math.toRadians(btheta))*bmag;
        return ax*bx + ay*by;
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

    public static boolean lessThanOrEqualTo(double a, double b) {
        int c = Double.compare(a, b);
        return c <= 0;
    }

    public static boolean lessThan(double a, double b) {
        int c = Double.compare(a, b);
        return c < 0;
    }

    public static boolean greaterThanOrEqualTo(double a, double b) {
        int c = Double.compare(a, b);
        return c >= 0;
    }

    public static boolean greaterThan(double a, double b) {
        int c = Double.compare(a, b);
        return c > 0;
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
            rmat.m21 = s;
            rmat.m12 = -s;
            break;
        case Y:
            rmat.m00 = c;
            rmat.m22 = c;
            rmat.m02 = s;
            rmat.m20 = -s;
            break;
        case Z:
            rmat.m00 = c;
            rmat.m11 = c;
            rmat.m10 = s;
            rmat.m01 = -s;
            break;
        default:
            throw new IllegalArgumentException("Invalid Axis");
        }
        return rmat;
    }

    public static Matrix4f createTransMatrix(double ix, double iy, double iz) {
        Matrix4f imat = new Matrix4f(); // identity
        imat.m03 = (float) ix;
        imat.m13 = (float) iy;
        imat.m23 = (float) iz;
        return imat.transpose(imat);
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
