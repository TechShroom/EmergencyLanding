/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <http://techshoom.com>
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