package emergencylanding.k.library.util;

import java.awt.geom.Rectangle2D;

import emergencylanding.k.library.util.Maths.Axis;
import emergencylanding.k.library.util.Maths.Geometry;
import emergencylanding.k.library.util.interfaces.ICollidable;

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
        box.setBounds(x, y, width + x, height + y);
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
            return box.equals(bb.box) && roll == bb.roll && yaw == bb.yaw
                    && pitch == bb.pitch;
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
            rot = roll;
            bbrot = bb.roll;
        } else if (ax == Axis.Y) {
            rot = yaw;
            bbrot = bb.yaw;
        } else if (ax == Axis.Z) {
            rot = pitch;
            bbrot = bb.pitch;
        }
        if (rot == bbrot) {
            Rectangle2D rbox = Geometry.rotateRect(box.asRect(), rot), bbrbox = Geometry
                    .rotateRect(bb.box.asRect(), bbrot);
            System.err.println(rbox + " collide " + bbrbox);
            return rbox.intersects(bbrbox);
        }
        /*
         * Old entity rot code
         * 
         * // TODO: needs to handle rotation.
         * 
         * double xCenterThis = this.getX() + this.getTex().getWidth(); double
         * yCenterThis = this.getY() + this.getTex().getHeight(); double
         * xCenterOther = other.getX() + other.getTex().getWidth(); double
         * yCenterOther = other.getY() + other.getTex().getHeight();
         * 
         * double gapX = xCenterOther - xCenterThis; double gapY = yCenterOther
         * - yCenterThis;
         * 
         * double angleToOther = Math.atan2(gapY, gapX);
         * 
         * double x_newGap = Maths.projectLineAlongSurface(this.getPitch(),
         * angleToOther, Math.sqrt(gapX * gapX + gapY * gapY), false); double
         * y_newGap = Maths.projectLineAlongSurface(this.getPitch(),
         * angleToOther, Math.sqrt(gapX * gapX + gapY * gapY), true);
         * 
         * double thisXLenOnNewGrid = this.getTex().getWidth(); double
         * thisYLenOnNewGrid = this.getTex().getHeight();
         * 
         * double otherXLenOnNewGrid = Maths.projectLineAlongSurface(
         * this.getPitch(), other.getPitch(), other.getTex().getWidth(), false)
         * + Maths.projectLineAlongSurface(this.getPitch(), other.getPitch(),
         * other.getTex().getHeight(), true); double otherYLenOnNewGrid =
         * Maths.projectLineAlongSurface( this.getPitch(), other.getPitch(),
         * other.getTex().getWidth(), true) +
         * Maths.projectLineAlongSurface(this.getPitch(), other.getPitch(),
         * other.getTex().getHeight(), false);
         * 
         * System.err.println(y_newGap + " x " + thisYLenOnNewGrid + " " +
         * otherYLenOnNewGrid); System.err.println(x_newGap + " y " +
         * thisXLenOnNewGrid + " " + otherXLenOnNewGrid); return (x_newGap <
         * thisXLenOnNewGrid / 2 + otherXLenOnNewGrid / 2 && y_newGap <
         * thisYLenOnNewGrid / 2 + otherYLenOnNewGrid / 2);
         */

        System.err.println(box + " collide " + bb.box);
        // best guess for now
        return box.intersects(bb.box);
    }

    public double getRoll() {
        return roll;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getX() {
        return box.getMinX();
    }

    public void setX(double x) {
        box.setMaxX(x + getWidth());
        box.setMinX(x);
    }

    public double getY() {
        return box.getMinY();
    }

    public void setY(double y) {
        box.setMaxY(y + getHeight());
        box.setMinY(y);
    }

    public double getWidth() {
        return box.getMaxX() - box.getMinX();
    }

    public void setWidth(double width) {
        box.setMaxX(box.getMinX() + width);
    }

    public double getHeight() {
        return box.getMaxY() - box.getMinY();
    }

    public void setHeight(double height) {
        box.setMaxY(box.getMinY() + height);
    }

    @Override
    public BoundingBox getBB() {
        return this;
    }
}
