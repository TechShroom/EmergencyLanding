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

import com.techshroom.emergencylanding.imported.Color;
import com.techshroom.emergencylanding.library.internalstate.GravEntity;
import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.lwjgl.tex.ColorTexture;

public class TestGravEntity extends GravEntity {

    private boolean bounce;

    public TestGravEntity(World w) {
        super(w, 100, 800, 0, new ColorTexture(Color.RED), 0.1f);
    }

    @Override
    public void updateOnTick(float delta) {
        if (this.pos.y <= 0 && !this.bounce) {
            this.bounce = true;
            setYVel(-this.vel.y);
        }
        if (this.bounce && this.pos.y > 0) {
            this.bounce = false;
        }
        super.updateOnTick(delta);
    }

}
