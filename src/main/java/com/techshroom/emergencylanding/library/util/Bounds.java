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
    public abstract void setBounds(Point2D[] bounds)
            throws IllegalArgumentException;

    public abstract Point2D[] getBounds();

    @Override
    public String toString() {
        return getClass().getName() + "["
                + Arrays.toString(Geometry.pointsAsDoubles(getBounds())) + "]";
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