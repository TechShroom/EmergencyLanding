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

import com.techshroom.emergencylanding.library.util.Maths.Axis;
import com.techshroom.emergencylanding.library.util.Maths.Geometry;
import com.techshroom.emergencylanding.library.util.interfaces.ICollidable;

public class BoundingBox implements ICollidable<BoundingBox> {

    protected Bounds2D box = new Bounds2D();
    /**
     * X Axis rotation: roll<br>
     * Y Axis rotation: yaw<br>
     * Z Axis rotation: pitch
     */
    protected double roll = 0, yaw = 0, pitch = 0;

    public BoundingBox() {
        // does nothing, leaves at defaults
    }

    public BoundingBox(double x, double y, double width, double height,
            double roll, double yaw, double pitch) {
        modBox(x, y, width, height, roll, yaw, pitch);
    }

    public void modBox(double x, double y, double width, double height,
            double roll, double yaw, double pitch) {
        this.box.setBounds(x, y, width + x, height + y);
        this.roll = roll;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o || super.equals(o)) {
            return true;
        }
        if (o instanceof BoundingBox) {
            BoundingBox bb = (BoundingBox) o;
            return this.box.equals(bb.box) && this.roll == bb.roll
                    && this.yaw == bb.yaw && this.pitch == bb.pitch;
        }
        return false;
    }

    @Override
    public boolean collidesWith(ICollidable<? extends ICollidable<?>> ic) {
        return collidesWith(ic.getBB());
    }

    /**
     * <p>
     * Calls {@link #collidesWith(BoundingBox, Axis)} with {@link Axis#X}.
     * </p>
     */
    @Override
    public boolean collidesWith(BoundingBox bb) {
        return collidesWith(bb, Axis.Z);
    }

    /**
     * Checks for bounding box collision, using the specified axis as the
     * rotation.
     * 
     * @param bb
     *            - a bounding box object for checking
     * @param ax
     *            - the axis to rotate on
     * @return true if the bounding boxes collide
     */
    public boolean collidesWith(BoundingBox bb, Axis ax) {
        double rot = 0, bbrot = 0;
        if (ax == Axis.X) {
            rot = this.roll;
            bbrot = bb.roll;
        } else if (ax == Axis.Y) {
            rot = this.yaw;
            bbrot = bb.yaw;
        } else if (ax == Axis.Z) {
            rot = this.pitch;
            bbrot = bb.pitch;
        }
        if (rot == bbrot) {
            Rectangle2D rbox = Geometry.rotateRect(this.box.asRect(), rot),
                    bbrbox = Geometry.rotateRect(bb.box.asRect(), bbrot);
            return rbox.intersects(bbrbox);
        }
        // bounds return array of size 2, so we need to reconstruct them
        Point2D[] boxA = this.box.getBounds(), boxB = bb.box.getBounds();
        Point2D[] tmpA = new Point2D[4], tmpB = new Point2D[4];
        // 0: LL 1: UL 2: LR 3: UR
        tmpA[0] = boxA[0]; // reuse
        tmpA[3] = boxA[1]; // reuse
        tmpA[1] = new Point2D.Double(boxA[0].getX(), boxA[1].getY());
        tmpA[2] = new Point2D.Double(boxA[1].getX(), boxA[0].getY());
        tmpB[0] = boxB[0]; // reuse
        tmpB[3] = boxB[1]; // reuse
        tmpB[1] = new Point2D.Double(boxB[0].getX(), boxB[1].getY());
        tmpB[2] = new Point2D.Double(boxB[1].getX(), boxB[0].getY());
        // left: LL to UL right: LR to UR top: UL to UR bottom: LL to LR
        LineSeg lA = new LineSeg(tmpA[0], tmpA[1]),
                rA = new LineSeg(tmpA[2], tmpA[3]),
                tA = new LineSeg(tmpA[1], tmpA[3]),
                bA = new LineSeg(tmpA[0], tmpA[2]);
        LineSeg lB = new LineSeg(tmpB[0], tmpB[1]),
                rB = new LineSeg(tmpB[2], tmpB[3]),
                tB = new LineSeg(tmpB[1], tmpB[3]),
                bB = new LineSeg(tmpB[0], tmpB[2]);
        return lA.collidesWith(bB) || rA.collidesWith(tB) || bA.collidesWith(lB)
                || tA.collidesWith(rB);
    }

    private static class LineSeg implements ICollidable<LineSeg> {

        Point2D s, e;

        private LineSeg(Point2D start, Point2D end) {
            this.s = start;
            this.e = end;
        }

        @Override
        public BoundingBox getBB() {
            return null; // we don't have BBs for lines
        }

        @Override
        public boolean
                collidesWith(ICollidable<? extends ICollidable<?>> other) {
            return (other instanceof LineSeg) ? collidesWith((LineSeg) other)
                    : false;
        }

        private static final double ERROR = Maths.med,
                ERROR_PLUS_ONE = ERROR + 1; // close enough, jeez

        @Override
        public boolean collidesWith(LineSeg other) {
            // calculate the denom of a certain fraction, see
            // http://devmag.org.za/2009/04/13/basic-collision-detection-in-2d-part-1/
            // for more info
            // then make sure between 0 and 1 for on segment
            double denom = ((other.e.getY() - other.s.getY())
                    * (this.e.getX() - this.s.getX()))
                    - ((other.e.getX() - other.s.getX())
                            * (this.e.getY() - this.s.getY()));
            if (denom == 0) {
                return false;
            }
            double ua = (((other.e.getX() - other.s.getX())
                    * (this.s.getY() - other.s.getY()))
                    - ((other.e.getY() - other.s.getY())
                            * (this.s.getX() - other.s.getX())))
                    / denom;
            /*
             * The following 3 lines are only necessary if we are checking line
             * segments instead of infinite-length lines
             */
            double ub = (((this.e.getX() - this.s.getX())
                    * (this.s.getY() - other.s.getY()))
                    - ((this.e.getY() - this.s.getY())
                            * (this.s.getX() - other.s.getX())))
                    / denom;
            ua += 0.0;
            ub += 0.0;
            if (Maths.lessThan(ua, -ERROR)
                    || Maths.greaterThan(ua, ERROR_PLUS_ONE)
                    || Maths.lessThan(ub, -ERROR)
                    || Maths.greaterThan(ub, ERROR_PLUS_ONE)) {
                if (LUtils.debugLevel > 1) {
                    System.err.println("[Begin]");
                    if (Maths.lessThan(ua, -ERROR)) {
                        System.err.println(
                                "ua " + ua + " was less than " + (-ERROR));
                    }
                    if (Maths.greaterThan(ua, ERROR_PLUS_ONE)) {
                        System.err.println("ua " + ua + " was greater than 1");
                    }
                    if (Maths.lessThan(ub, -ERROR)) {
                        System.err.println(
                                "ub " + ub + " was less than " + (-ERROR));
                    }
                    if (Maths.greaterThan(ub, ERROR_PLUS_ONE)) {
                        System.err.println("ub " + ub + " was greater than 1");
                    }
                    System.err.println("[End]");
                }
                return false;
            }
            if (LUtils.debugLevel > 1) {
                System.err.println("We got some collisions of lines here: " + ua
                        + ":" + ub);
            }
            return true;
        }

        @Override
        public String toString() {
            return "[" + pstr(this.s) + "->" + pstr(this.e) + "]";
        }

        private static String pstr(Point2D p) {
            return "(" + p.getX() + ", " + p.getY() + ")";
        }
    }

    public double getRoll() {
        return this.roll;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }

    public double getYaw() {
        return this.yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public double getPitch() {
        return this.pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getX() {
        return this.box.getMinX();
    }

    public void setX(double x) {
        this.box.setMaxX(x + getWidth());
        this.box.setMinX(x);
    }

    public double getY() {
        return this.box.getMinY();
    }

    public void setY(double y) {
        this.box.setMaxY(y + getHeight());
        this.box.setMinY(y);
    }

    public double getWidth() {
        return this.box.getMaxX() - this.box.getMinX();
    }

    public void setWidth(double width) {
        this.box.setMaxX(this.box.getMinX() + width);
    }

    public double getHeight() {
        return this.box.getMaxY() - this.box.getMinY();
    }

    public void setHeight(double height) {
        this.box.setMaxY(this.box.getMinY() + height);
    }

    @Override
    public BoundingBox getBB() {
        return this;
    }
}
