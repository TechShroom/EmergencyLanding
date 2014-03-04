package emergencylanding.k.library.util;

import java.awt.geom.Rectangle2D;

import emergencylanding.k.library.util.interfaces.ICollidable;

public class BoundingBox implements ICollidable<BoundingBox> {
    protected Rectangle2D.Double box = new Rectangle2D.Double();
    /**
     * X Axis rotation: roll<br>
     * Y Axis rotation: yaw<br>
     * Z Axis rotation: pitch
     */
    protected double roll, yaw, pitch;

    public BoundingBox(double x, double y, double width, double height,
            double roll, double yaw, double pitch) {
        modBox(x, y, width, height, roll, yaw, pitch);
    }

    public void modBox(double x, double y, double width, double height,
            double roll, double yaw, double pitch) {
        box.setRect(x, y, width, height);
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

    @Override
    public boolean collidesWith(BoundingBox bb) {
        // ignores rot for now
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
        return box.getX();
    }

    public void setX(double x) {
        box.x = x;
    }

    public double getY() {
        return box.getY();
    }

    public void setY(double y) {
        box.y = y;
    }

    public double getWidth() {
        return box.getWidth();
    }

    public void setWidth(double width) {
        box.width = width;
    }

    public double getHeight() {
        return box.getHeight();
    }

    public void setHeight(double height) {
        box.height = height;
    }

    @Override
    public BoundingBox getBB() {
        return this;
    }
}
