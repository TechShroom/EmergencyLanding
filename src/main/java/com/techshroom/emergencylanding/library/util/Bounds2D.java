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
package com.techshroom.emergencylanding.library.util;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A 2D implementation of {@link Bounds}.
 * 
 * @author Kenzie Togami
 */
public class Bounds2D extends Bounds {

    private double minx, miny, maxx, maxy;

    // little cache that stores values until bounds change
    private transient Rectangle2D cache = null;

    /**
     * Convert this Bounds2D to a {@link Rectangle2D}. This operation is cached.
     * 
     * @return a {@link Rectangle2D} that represents this Bounds2D object
     */
    public Rectangle2D asRect() {
        return asRect(null);
    }

    /**
     * Convert this Bounds2D to a {@link Rectangle2D}. This operation is cached.
     * 
     * @param dest
     *            - the Rectangle2D to store the values into, Can be
     *            <code>null</code>, which creates a new Rectangle2D.
     * @return a {@link Rectangle2D} that represents this Bounds2D object
     */
    public Rectangle2D asRect(Rectangle2D dest) {
        if (dest == null) {
            dest = new Rectangle2D.Double();
        }
        if (this.cache == null) {
            this.cache = new Rectangle2D.Double(this.minx, this.miny,
                    this.maxx - this.minx, this.maxy - this.miny);
        }
        dest.setRect(this.cache);
        return dest;
    }

    /**
     * @return the maxx
     */
    public double getMaxX() {
        return this.maxx;
    }

    /**
     * @return the maxy
     */
    public double getMaxY() {
        return this.maxy;
    }

    /**
     * @return the minx
     */
    public double getMinX() {
        return this.minx;
    }

    /**
     * @return the miny
     */
    public double getMinY() {
        return this.miny;
    }

    @Override
    public boolean intersects(Bounds b) {
        if (b instanceof Bounds2D) {
            return intersects((Bounds2D) b);
        }
        return false;
    }

    public boolean intersects(Bounds2D box) {
        double x = box.minx, y = box.miny, x2 = box.maxx, y2 = box.maxy;
        if (isEmpty() || box.isEmpty()) {
            return false;
        }
        double x0 = this.minx, x1 = this.maxx;
        double y0 = this.miny, y1 = this.maxy;
        return (x2 >= x0 && y2 >= y0 && x <= x1 && y <= y1);
    }

    /**
     * Force a cache recalculation. This may be removed in a future release if
     * the cache is removed.
     */
    public void invalidateCache() {
        this.cache = null;
    }

    public boolean isEmpty() {
        return (this.maxx - this.minx) == 0 || (this.maxy - this.miny) == 0;
    }

    /**
     * Two points define a Bounds2D.
     */
    @Override
    public int pointCount() {
        return 2;
    }

    @Override
    public void setBounds(Point2D[] bounds) {
        if (bounds.length != pointCount()) {
            throw new IllegalArgumentException("requires 2 points only");
        }
        this.minx = bounds[0].getX();
        this.miny = bounds[0].getY();
        this.maxx = bounds[1].getX();
        this.maxy = bounds[1].getY();
        this.cache = null;
    }

    /**
     * @param maxx
     *            the maxx to set
     */
    public void setMaxX(double maxx) {
        this.maxx = maxx;
    }

    /**
     * @param maxy
     *            the maxy to set
     */
    public void setMaxY(double maxy) {
        this.maxy = maxy;
    }

    /**
     * @param minx
     *            the minx to set
     */
    public void setMinX(double minx) {
        this.minx = minx;
    }

    /**
     * @param miny
     *            the miny to set
     */
    public void setMinY(double miny) {
        this.miny = miny;
    }

    @Override
    public Point2D[] getBounds() {
        return new Point2D[] { new Point2D.Double(this.minx, this.miny),
                new Point2D.Double(this.maxx, this.maxy) };
    }
}