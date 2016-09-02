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
package com.techshroom.emergencylanding.library.lwjgl.render;

import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.internalstate.Victor;

public abstract class Render<T extends ELEntity> {

    /**
     * Renders the given entity at it's location, yaw, and pitch.
     * 
     * @param entity
     */
    public void doRender(T entity, Victor interpolated) {
        doRender(entity, interpolated.x, interpolated.y, interpolated.z);
    }

    /**
     * Renders an entity with the <i>given</i> arguments instead of the
     * entity's.
     * 
     * @param entity
     *            - the entity to render
     * @param posX
     *            - the positionX to render at
     * @param posY
     *            - the positionY to render at
     * @param posZ
     *            - the positionZ to render at
     */
    public abstract void doRender(T entity, float posX, float posY, float posZ);
}
