package com.techshroom.emergencylanding.library.internalstate;

import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;
import com.techshroom.emergencylanding.library.util.Maths;

public abstract class EntityCollide extends ELEntity {

    public EntityCollide(World w, float posX, float posY, float posZ,
            ELTexture texture) {
        super(w, posX, posY, posZ, texture);
    }

    @Override
    public void updateOnTick(float delta) {
        /*
         * super.updateOnTick(delta); for (ELEntity other : w.getEntityList()) {
         * if (other instanceof EntityCollide) { testCollide((EntityCollide)
         * other); } }
         */
    }

    public boolean testCollide(EntityCollide other) {
        double xCenterThis = this.getX() + this.getTex().getWidth();
        double yCenterThis = this.getY() + this.getTex().getHeight();
        double xCenterOther = other.getX() + other.getTex().getWidth();
        double yCenterOther = other.getY() + other.getTex().getHeight();

        double gapX = Math.abs(xCenterOther - xCenterThis);
        double gapY = Math.abs(yCenterOther - yCenterThis);

        double angleToOther = Math.atan2(gapY, gapX);

        double xGapBetweenObjs = Maths.projectLineAlongSurface(this.getRoll(),
                Math.pow(gapX * gapX + gapY * gapY, .5), gapX, false);
        double yGapBetweenObjs = Maths.projectLineAlongSurface(this.getRoll(),
                Math.pow(gapX * gapX + gapY * gapY, .5), gapY, false);

        double thisXLenOnNewGrid = this.getTex().getWidth();
        double thisYLenOnNewGrid = this.getTex().getHeight();

        return false; // for now
    }

    /*
     * public boolean testCollide(EntityCollide other) { double xCenterThis =
     * this.getX() + this.getTex().getWidth(); double yCenterThis = this.getY()
     * + this.getTex().getHeight(); double xCenterOther = other.getX() +
     * other.getTex().getWidth(); double yCenterOther = other.getY() +
     * other.getTex().getHeight();
     * 
     * double gapX = Math.abs(xCenterOther - xCenterThis); double gapY =
     * Math.abs(yCenterOther - yCenterThis);
     * 
     * double angleToOther = Math.atan2(gapY, gapX);
     * 
     * double x_newGap = Maths.projectLineAlongSurface(this.getPitch(),
     * angleToOther, Math.sqrt(gapX * gapX + gapY * gapY), false); double
     * y_newGap = Maths.projectLineAlongSurface(this.getPitch(), angleToOther,
     * Math.sqrt(gapX * gapX + gapY * gapY), true);
     * 
     * double thisXLenOnNewGrid = this.getTex().getWidth(); double
     * thisYLenOnNewGrid = this.getTex().getHeight();
     * 
     * double otherXLenOnNewGrid =
     * Maths.projectLineAlongSurface(this.getPitch(), other.getPitch(),
     * other.getTex().getWidth(), false) +
     * Maths.projectLineAlongSurface(this.getPitch(), other.getPitch(),
     * other.getTex().getHeight(), true); double otherYLenOnNewGrid =
     * Maths.projectLineAlongSurface(this.getPitch(), other.getPitch(),
     * other.getTex().getWidth(), true) +
     * Maths.projectLineAlongSurface(this.getPitch(), other.getPitch(),
     * other.getTex().getHeight(), false);
     * 
     * boolean xTouch = x_newGap < (thisXLenOnNewGrid / 2 + otherXLenOnNewGrid/
     * 2); boolean yTouch = y_newGap < (thisYLenOnNewGrid / 2 +
     * otherYLenOnNewGrid / 2); System.out.println(); return (xTouch && yTouch);
     * }
     */
}