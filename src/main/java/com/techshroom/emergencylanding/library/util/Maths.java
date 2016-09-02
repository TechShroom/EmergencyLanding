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

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;

import com.flowpowered.math.imaginary.Quaternionf;
import com.flowpowered.math.matrix.Matrix4f;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3f;

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
            double x = r.getX(), y = r.getY(), w = r.getWidth(), h = r.getHeight();
            // clear rect
            r.setRect(0, 0, 0, 0);
            // get points
            Point2D[] points = new Point2D[] { new Point2D.Double(x, y), new Point2D.Double(w + x, h + y) };
            // calc cos/sin
            double s = qsin(theta), c = qcos(theta);
            // expand rect to fit
            for (Point2D p : points) {
                p.setLocation((p.getX() * c) - (p.getY() * s), (p.getX() * s) + (p.getY() * c));
            }
            r.setRect(points[0].getX(), points[0].getY(), points[1].getX() - points[0].getX(),
                    points[1].getY() - points[0].getY());
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
    public static double
            projectLineAlongSurface(double thetaSurface, double thetaLineToProject, double magnitude, boolean getY) {
        double dp = dotProductAngles(magnitude, thetaLineToProject, 1, thetaSurface);
        if (!getY) {
            System.out.println(dp);
            return dp * Math.cos(Math.toRadians(thetaSurface));
        } else {
            System.out.println(dp);
            return dp * Math.sin(Math.toRadians(thetaSurface));
        }

    }

    public static double
            projectLineAlongSurfaceXY(double xSurface, double ySurface, double xLine, double yLine, boolean DoY) {
        double dp =
                dotProduct(xLine, yLine, Maths.normalizeX(xSurface, ySurface), Maths.normalizeY(xSurface, ySurface));
        if (!DoY) {
            System.out.println(dp);
            return dp * xSurface;
        } else {
            System.out.println(dp);
            return dp * ySurface;
        }

    }

    public static double normalizeX(double x, double y) {
        double len_v = Math.sqrt(x * x + y * y);
        return x / len_v;
    }

    public static double normalizeY(double x, double y) {
        double len_v = Math.sqrt(x * x + y * y);
        return y / len_v;
    }

    public static double dotProduct(double ax, double ay, double bx, double by) {
        return ax * ay + bx * by;
    }

    public static double dotProductAngles(double amag, double atheta, double bmag, double btheta) {
        double ax = Math.cos(Math.toRadians(atheta)) * amag;
        double bx = Math.cos(Math.toRadians(btheta)) * bmag;
        double ay = Math.sin(Math.toRadians(atheta)) * amag;
        double by = Math.sin(Math.toRadians(btheta)) * bmag;
        return ax * bx + ay * by;
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
        X(new Vector3f(1, 0, 0)), Y(new Vector3f(0, 1, 0)), Z(new Vector3f(0, 0, 1));

        private final Vector3f unitVector;

        private Axis(Vector3f unit) {
            this.unitVector = unit;
        }

        public Vector3f getUnitVector() {
            return this.unitVector;
        }

    }

    public static Matrix4f createRotMatrix(double theta, Axis axis) {
        return Matrix4f.createRotation(Quaternionf.fromAngleDegAxis(theta, axis.unitVector));
    }

    public static Matrix4f createTransMatrix(double ix, double iy, double iz) {
        return Matrix4f.createTranslation(ix, iy, iz);
    }

    public static Matrix4f createScaleMatrix(double sx, double sy, double sz) {
        return Matrix4f.createScaling(sx, sy, sz, 1);
    }

    public static void storeMatrix(Matrix4f matrix, FloatBuffer buf) {
        float[] array = matrix.toArray(true);
        buf.put(array);
    }

    /* "quick" trig: shortcuts default values for accuracy and speed */
    /*
     * Worse under non-default values due to lookup taking time, but is small
     * enough to be negligible
     */

    private static double[] sin = (double[]) BetterArrays.createAndFill(double.class, 361, -5);
    private static double[] cos = (double[]) BetterArrays.createAndFill(double.class, 361, -5);
    private static double[] tan = (double[]) BetterArrays.createAndFill(double.class, 361, -5);
    private static int zero = 0, thirty = 30, fortyfive = 45, sixty = 60, ninety = 90, oneeighty = 180,
            twoseventy = 270;
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
        return (((int) theta) == theta && cos[(int) theta] != -5) ? cos[(int) theta] : Math.cos(Math.toRadians(theta));
    }

    public static double qsin(double theta) {
        theta = normalizeDeg(theta);
        return (((int) theta) == theta && sin[(int) theta] != -5) ? sin[(int) theta] : Math.sin(Math.toRadians(theta));
    }

    public static double qtan(double theta) {
        theta = normalizeDeg(theta);
        return (((int) theta) == theta && tan[(int) theta] != -5) ? tan[(int) theta] : Math.tan(Math.toRadians(theta));
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

    /**
     * 1 &rarr; 1st, 2 &rarr; 2nd, 3 &rarr; 3rd, etc.
     */
    public static String toOrdinalNumber(int i) {
        switch (i % 100) {
            // Special cases: 11, 12, 13 all use th.
            case 11:
            case 12:
            case 13:
                return i + "th";
            // Normal cases
            default:
                switch (i % 10) {
                    // X1 == st (1st, 21st)
                    case 1:
                        return i + "st";
                    // X2 == nd (2nd, 22nd)
                    case 2:
                        return i + "nd";
                    // X3 == rd (3rd, 23rd)
                    case 3:
                        return i + "rd";
                    // Rest == th (8th, 25th)
                    default:
                        return i + "th";
                }
        }
    }

    private static final String[] TENS_NAMES =
            { "", " ten", " twenty", " thirty", " forty", " fifty", " sixty", " seventy", " eighty", " ninety" };

    private static final String[] NUM_NAMES = { "", " one", " two", " three", " four", " five", " six", " seven",
            " eight", " nine", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen",
            " seventeen", " eighteen", " nineteen" };

    private static String convertLessThanOneThousand(int number) {
        String soFar;

        int mod100 = number % 100;
        if (mod100 < 20) {
            soFar = NUM_NAMES[mod100];
            number /= 100;
        } else {
            soFar = NUM_NAMES[number % 10];
            number /= 10;

            soFar = TENS_NAMES[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0)
            return soFar;
        return NUM_NAMES[number] + " hundred" + soFar;
    }

    public static String convertNumberToEnglish(long number) {
        // 0 to 999 999 999 999
        if (number == 0) {
            return "zero";
        }

        String snumber = Long.toString(number);

        // pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        // XXXnnnnnnnnn
        int billions = Integer.parseInt(snumber.substring(0, 3));
        // nnnXXXnnnnnn
        int millions = Integer.parseInt(snumber.substring(3, 6));
        // nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9, 12));

        String tradBillions = (billions == 0 ? "" : convertLessThanOneThousand(billions) + " billion ");
        String result = tradBillions;

        String tradMillions = (millions == 0 ? "" : convertLessThanOneThousand(millions) + " million ");
        result += tradMillions;

        String tradHundredThousands;
        switch (hundredThousands) {
            case 0:
                tradHundredThousands = "";
                break;
            case 1:
                tradHundredThousands = "one thousand ";
                break;
            default:
                tradHundredThousands = convertLessThanOneThousand(hundredThousands) + " thousand ";
        }
        result += tradHundredThousands;

        String tradThousand = convertLessThanOneThousand(thousands);
        result += tradThousand;

        // remove extra spaces!
        return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }

    public static Vector2d rotate(Vector2d v, double sin, double cos) {
        return new Vector2d(v.getX() * cos - v.getY() * sin, v.getX() * sin + v.getY() * cos);
    }

    public static int addWithOverflowChecks(int... ints) {
        int ret = 0;
        for (int i : ints) {
            try {
                ret = Math.addExact(ret, i);
            } catch (ArithmeticException err) {
                if (i < ret) {
                    // Overflow negative
                    return Integer.MIN_VALUE;
                } else {
                    // Overflow positive
                    return Integer.MAX_VALUE;
                }
            }
        }
        return ret;
    }

}
