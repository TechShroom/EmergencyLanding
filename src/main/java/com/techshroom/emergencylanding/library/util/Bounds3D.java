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

//TODO: Flesh out
public class Bounds3D extends Bounds {

    private double minx, miny, minz, maxx, maxy, maxz;

    /**
     * <p>
     * NB: Because there is no Point3D class so we can unify everything,
     * <strong> the third point's x defines minz and it's y defines
     * maxz.</strong>
     * </p>
     * {@inheritDoc}
     */
    @Override
    public void setBounds(Point2D[] bounds) {
        if (bounds.length != pointCount()) {
            throw new IllegalArgumentException("requires 3 points only");
        }
        this.minx = bounds[0].getX();
        this.miny = bounds[0].getY();
        this.maxx = bounds[1].getX();
        this.maxy = bounds[1].getY();
        this.minz = bounds[2].getX();
        this.maxz = bounds[2].getY();
    }

    /**
     * Three points define a Bounds3D.
     */
    @Override
    public int pointCount() {
        return 3;
    }

    @Override
    public boolean intersects(Bounds b) {
        throw new UnsupportedOperationException();
    }

    /**
     * <p>
     * NB: Because there is no Point3D class so we can unify everything,
     * <strong> the third point's x defines minz and it's y defines
     * maxz.</strong>
     * </p>
     * {@inheritDoc}
     */
    @Override
    public Point2D[] getBounds() {
        return new Point2D[] { new Point2D.Double(this.minx, this.miny), new Point2D.Double(this.maxx, this.maxy),
                new Point2D.Double(this.minz, this.maxz) };
    }
}