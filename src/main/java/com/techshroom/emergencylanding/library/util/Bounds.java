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
package com.techshroom.emergencylanding.library.util;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import com.techshroom.emergencylanding.library.util.Maths.Geometry;

public abstract class Bounds {

    /**
     * Sets the bounds for this Bounds object. The amount of bounds that may be
     * handed to this method is specified by {@link #pointCount()}.
     * 
     * @param bounds
     *            - the new bounds for this Bounds object
     * @throws IllegalArgumentException
     *             if the size of <code>bounds</code> does not match the value
     *             returned by {@link #pointCount()}.
     */
    public abstract void setBounds(Point2D[] bounds) throws IllegalArgumentException;

    public abstract Point2D[] getBounds();

    @Override
    public String toString() {
        return getClass().getName() + "[" + Arrays.toString(Geometry.pointsAsDoubles(getBounds())) + "]";
    }

    /**
     * A helper method that allows for easy transitions from {@link Rectangle2D}
     * to Bounds.
     * 
     * @param bounds
     *            - pairs of doubles that make up points.
     * @throws IllegalArgumentException
     *             if the size of <code>bounds</code> does not
     *             {@link #pointCount()} * 2 or if it is not divisible by 2.
     */
    public void setBounds(double... bounds) throws IllegalArgumentException {
        setBounds(Geometry.doublesAsPoints(bounds));
    }

    /**
     * 
     * @return the amount of points required to define this Bounds.
     */
    public abstract int pointCount();

    /**
     * Tests the two Bounds objects to see if they intersect each other. May
     * return false for testing different subclasses.
     * 
     * @return <code>true</code> if the interior of the <code>Shape</code> and
     *         the interior of the rectangular area intersect, or are both
     *         highly likely to intersect and intersection calculations would be
     *         too expensive to perform; <code>false</code> otherwise.
     */
    public abstract boolean intersects(Bounds b);
}