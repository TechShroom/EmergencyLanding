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

import com.techshroom.emergencylanding.imported.Color;
import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.lwjgl.tex.ColorTexture;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;
import com.techshroom.emergencylanding.library.util.BoundingBox;
import com.techshroom.emergencylanding.library.util.interfaces.ICollidable;

public abstract class ELEntity implements ICollidable<ELEntity> {

    protected Victor pos = new Victor(), posInter = new Victor(),
            vel = new Victor(), velInter = new Victor();
    protected BoundingBox box = new BoundingBox();
    protected float deltaT;
    protected float elapsed;
    private ELTexture tex;
    private boolean isDead;
    public World w = null;

    public ELEntity(World w) {
        setXYZ(0, 0, 0);
        setTex(ColorTexture.getColor(Color.RED));
        this.w = w;
    }

    public ELEntity(World w, ELTexture texture) {
        setXYZ(0, 0, 0);
        setTex(texture);
        this.w = w;
    }

    public ELEntity(World w, float posX, float posY, float posZ,
            ELTexture texture) {
        setXYZ(posX, posY, posZ);
        setTex(texture);
        this.w = w;
    }

    public ELEntity(World w, float posX, float posY, float posZ, float xVel,
            float yVel, float zVel, ELTexture texture) {
        setXYZ(posX, posY, posZ);
        setXYZVel(xVel, yVel, zVel);
        setTex(texture);
        this.w = w;
    }

    public ELEntity(World w, float posX, float posY, float posZ, float xVel,
            float yVel, float zVel, float pitchRot, float yawRot, float rollRot,
            ELTexture texture) {
        setXYZ(posX, posY, posZ);
        setXYZVel(xVel, yVel, zVel);
        this.box.setPitch(pitchRot);
        this.box.setYaw(yawRot);
        this.box.setRoll(rollRot);
        setTex(texture);
        this.w = w;
    }

    /**
     * Update positions based on velocities. In superclasses, overridden to do..
     * stuff. You know. stuff.
     * 
     * @param delta
     *            - the float value of the time elapsed to update.
     */
    public void updateOnTick(float delta) {
        this.elapsed = 0;

        setXYZ(this.pos.x + this.vel.x * delta, this.pos.y + this.vel.y * delta,
                this.pos.z + this.vel.z * delta);

        this.deltaT = delta;
    }

    /**
     * Sets X, Y, and Z positions all in one. wooo.
     * 
     * @param newX
     *            - new posX
     * @param newY
     *            - new posY
     * @param newZ
     *            - new posZ
     */
    public void setXYZ(float newX, float newY, float newZ) {
        if (!this.pos.init) {
            this.pos.init(newX, newY, newZ);
        } else {
            this.pos.update(newX, newY, newZ);
        }
        this.box.setX(newX - getWidth() / 2);
        this.box.setY(newY - getHeight() / 2);
    }

    /**
     * Sets relative X, Y, and Z positions all in one. Even more wooooo.
     * 
     * @param relX
     *            - relative change in posX.
     * @param relY
     *            - relative change in posY.
     * @param relZ
     *            - relative change in posZ.
     */
    public void setRelativeXYZ(float relX, float relY, float relZ) {
        setXYZ(this.pos.x + relX, this.pos.y + relY, this.pos.z + relZ);
    }

    /**
     * Sets X, Y, and Z velocities all in one. wow, neat.
     * 
     * @param newXVel
     *            - new velX
     * @param newYVel
     *            - new velY
     * @param newZVel
     *            - new velZ
     */
    public void setXYZVel(float newXVel, float newYVel, float newZVel) {
        this.vel.update(newXVel, newYVel, newZVel);
    }

    /**
     * Sets relative X, Y, and Z velocities all in one. The most wowiest.
     * 
     * @param relXVel
     *            - Relative velX
     * @param relYVel
     *            - Relative velY
     * @param relZVel
     *            - Relative velZ
     */
    public void setRelativeXYZVel(float relXVel, float relYVel, float relZVel) {
        setXYZVel(this.vel.x + relXVel, this.vel.y + relYVel,
                this.vel.z + relZVel);
    }

    /**
     * Get entity x-pos
     * 
     * @return thisEntity.posX
     */
    public float getX() {
        return this.pos.x;
    }

    /**
     * Get entity y-pos
     * 
     * @return thisEntity.posY
     */
    public float getY() {
        return this.pos.y;
    }

    /**
     * Get entity z-pos
     * 
     * @return thisEntity.posZ
     */
    public float getZ() {
        return this.pos.z;
    }

    public double getPitch() {
        return this.box.getPitch();
    }

    public double getYaw() {
        return this.box.getYaw();
    }

    public double getRoll() {
        return this.box.getRoll();
    }

    public void setYaw(double newyaw) {
        this.box.setYaw(newyaw);
    }

    public void setRoll(double newroll) {
        this.box.setRoll(newroll);
    }

    public void setPitch(double newpitch) {
        this.box.setPitch(newpitch);
    }

    /**
     * Get entity x-vel
     * 
     * @return thisEntity.velX
     */
    public float getXVel() {
        return this.vel.x;
    }

    /**
     * Get entity y-vel
     * 
     * @return thisEntity.velY
     */
    public float getYVel() {
        return this.vel.y;
    }

    /**
     * Get entity z-vel
     * 
     * @return thisEntity.velZ
     */
    public float getZVel() {
        return this.vel.z;
    }

    /**
     * Set posX to newX
     * 
     * @param newX
     *            the new value for posX
     */
    public void setXPos(float newX) {
        setXYZ(newX, this.pos.y, this.pos.z);
    }

    /**
     * Set posY to newY
     * 
     * @param newY
     *            the new value for posY
     */
    public void setYPos(float newY) {
        setXYZ(this.pos.x, newY, this.pos.z);
    }

    /**
     * Set posZ to newZ
     * 
     * @param newZ
     *            the new value for posZ
     */
    public void setZPos(float newZ) {
        setXYZ(this.pos.x, this.pos.y, newZ);
    }

    /**
     * Set velX to newXVel
     * 
     * @param newXVel
     *            the new value for velX
     */
    public void setXVel(float newXVel) {
        setXYZVel(newXVel, this.vel.y, this.vel.z);
    }

    /**
     * Set velY to newYVel
     * 
     * @param newYVel
     *            the new value for velY
     */
    public void setYVel(float newYVel) {
        setXYZVel(this.vel.x, newYVel, this.vel.z);
    }

    /**
     * Set velZ to newZVel
     * 
     * @param newZVel
     *            the new value for velZ
     */
    public void setZVel(float newZVel) {
        setXYZVel(this.vel.x, newZVel, this.vel.z);
    }

    public void markForDeath() {
        this.isDead = true;
    }

    public boolean isDead() {
        return this.isDead;
    }

    public void interpolate(float elap) {
        synchronized (this.posInter) {
            synchronized (this.pos) {
                this.posInter = this.pos;
            }
        }
        synchronized (this.velInter) {
            this.velInter = this.vel;
        }
    }

    public Victor getInterpolated() {
        synchronized (this.posInter) {
            synchronized (this.velInter) {
                this.posInter.update(this.posInter.x + this.velInter.x,
                        this.posInter.y + this.velInter.y,
                        this.posInter.z + this.velInter.z);
                return this.posInter;
            }
        }
    }

    public Victor getPosition() {
        synchronized (this.pos) {
            return this.pos;
        }
    }

    public void setTex(ELTexture tex) {
        this.tex = tex;
        this.box.setWidth(tex.getWidth());
        this.box.setHeight(tex.getHeight());
    }

    public ELTexture getTex() {
        return this.tex;
    }

    public double getWidth() {
        return this.box.getWidth();
    }

    public double getHeight() {
        return this.box.getHeight();
    }

    @Override
    public BoundingBox getBB() {
        return this.box;
    }

    @Override
    public boolean collidesWith(ICollidable<? extends ICollidable<?>> other) {
        return getBB().collidesWith(other);
    }

    @Override
    public boolean collidesWith(ELEntity other) {
        return collidesWith((ICollidable<ELEntity>) other);
    }
}