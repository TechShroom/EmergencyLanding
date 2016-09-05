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
package com.techshroom.emergencylanding.library.internalstate;

import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;

public class GravEntity extends ELEntity {

    private float grav;

    public GravEntity(World w, float posX, float posY, float posZ, ELTexture texture) {
        super(w, posX, posY, posZ, texture);
        this.grav = 9.8f;
    }

    /**
     * @param w
     *            - the World
     * @param posX
     *            - x position
     * @param posY
     *            - y position
     * @param posZ
     *            - z position
     * @param texture
     *            - texture, duh.
     * @param gravity
     *            - Acceleration due to gravity
     */
    public GravEntity(World w, float posX, float posY, float posZ, ELTexture texture, float gravity) {
        super(w, posX, posY, posZ, texture);
        this.grav = gravity;
    }

    @Override
    public void updateOnTick(float delta) {
        super.updateOnTick(delta);
        setRelativeXYZVel(0, -this.grav, 0);
    }
}
