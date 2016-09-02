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
package com.techshroom.emergencylanding.library.util.interfaces;

import com.techshroom.emergencylanding.library.util.BoundingBox;

/**
 * ICollidable provides an interface over which many different objects can agree
 * on collisions. It can be applied for entities, buttons, and more.
 * 
 * @author Kenzie Togami
 *
 */
public interface ICollidable<T> {

    /**
     * Gets the {@link BoundingBox} that can be used for collisions.
     * 
     * @return the rotation and coordinate data in one concise object
     */
    public BoundingBox getBB();

    /**
     * Determines if this ICollidable collides with the other ICollidable. Most
     * implementations should call upon the {@link #getBB()} method and use the
     * returned {@link BoundingBox} to calculate intersection via
     * {@link BoundingBox#collidesWith(ICollidable)}
     * 
     * @param other
     *            - the ICollidable to check collision against
     * @return true if this ICollidable collides with the other ICollidable
     */
    public boolean collidesWith(ICollidable<? extends ICollidable<?>> other);

    /**
     * Implementations should flesh this out this if they use special handling
     * for themselves.
     * 
     * @param other
     *            - the T instance to check against
     * @return true if this T collides with the other T
     */
    public boolean collidesWith(T other);
}
