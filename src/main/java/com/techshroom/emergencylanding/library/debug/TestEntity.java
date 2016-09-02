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
package com.techshroom.emergencylanding.library.debug;

import java.util.Random;

import com.flowpowered.math.vector.Vector2i;
import com.techshroom.emergencylanding.imported.Color;
import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.tex.ColorTexture;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;
import com.techshroom.emergencylanding.library.util.BetterArrays;
import com.techshroom.emergencylanding.library.util.DrawableUtils;

public class TestEntity extends ELEntity {

    private Random rnd = new Random();
    boolean onScreenX = true, onScreenY = true;
    private static ELTexture[] texmix = { new ColorTexture(Color.BLUE), new ColorTexture(Color.RED),
            new ColorTexture(Color.GREEN), new ColorTexture(Color.MAGENTA) };

    public TestEntity(World w) {
        super(w, 0, 0, 0, TestEntity.texmix[0]);
    }

    @Override
    public void updateOnTick(float delta) {
        // For test entities, we can assume there's no other windows for now.
        DisplayLayer layer = DisplayLayer.CREATED.values().iterator().next();
        Vector2i size = DrawableUtils.getWindowSize(layer);
        if (this.pos.x > size.getX() || this.pos.x < 0) {
            setXPos(!(this.pos.x > size.getX()) ? size.getX() : 0);
        }
        if (this.pos.y > size.getY() || this.pos.y < 0) {
            setYPos(!(this.pos.y > size.getY()) ? size.getY() : 0);
        }
        if (Math.sqrt(this.vel.y * this.vel.y + this.vel.x * this.vel.x) < 0.5) {
            setTex(BetterArrays.randomArray(TestEntity.texmix)[0]);
        }
        super.updateOnTick(delta);
        doRandomVelocityChange();
        setYaw(getYaw() + 1);
    }

    private void doRandomVelocityChange() {
        if (this.rnd.nextFloat() < 0.9) {
            return;
        }
        if (this.vel.y < 10 && this.vel.y > 0) {
            this.vel.lastY = this.vel.y;
            this.vel.y += this.rnd.nextDouble();
            this.vel.y *= this.rnd.nextBoolean() ? -1 : 1;
        } else {
            this.vel.lastY = this.vel.y;
            this.vel.y = this.vel.y > 10 ? 10 : 0.1f;
        }
        if (this.vel.x < 10 && this.vel.x > 0) {
            this.vel.lastX = this.vel.x;
            this.vel.x += this.rnd.nextDouble();
            this.vel.x *= this.rnd.nextBoolean() ? -1 : 1;
        } else {
            this.vel.lastX = this.vel.x;
            this.vel.x = this.vel.x > 10 ? 10 : 0.1f;
        }
    }
}
