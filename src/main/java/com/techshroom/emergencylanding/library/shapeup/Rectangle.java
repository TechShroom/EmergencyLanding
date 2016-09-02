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
package com.techshroom.emergencylanding.library.shapeup;

import java.util.List;
import java.util.Objects;

import com.flowpowered.math.TrigMath;
import com.flowpowered.math.vector.Vector2d;
import com.google.common.base.MoreObjects;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.techshroom.emergencylanding.library.shapeup.generated.PointSet;
import com.techshroom.emergencylanding.library.util.Maths;

public class Rectangle implements Shape.TwoDimensional<Rectangle>, PointSet.Four<Vector2d> {

    public static Rectangle fromLengthAndWidth(double width, double height) {
        return new Rectangle(PointSet.Four.create(Vector2d.ZERO, new Vector2d(width, 0), new Vector2d(width, height),
                new Vector2d(0, height)), width, height, 0);
    }

    public static Rectangle fromLengthWidthAndRadians(double width, double height, double radians) {
        return new Rectangle(computeRotatedPoints(width, height, radians), width, height, radians);
    }

    private static PointSet.Four<Vector2d> computeRotatedPoints(double width, double height, double radians) {
        Vector2d bottomLeft = Vector2d.ZERO;
        Vector2d bottomRight = new Vector2d(width, 0);
        Vector2d topRight = new Vector2d(width, height);
        Vector2d topLeft = new Vector2d(0, height);
        double sin = TrigMath.sin(radians);
        double cos = TrigMath.cos(radians);
        bottomLeft = Maths.rotate(bottomLeft, sin, cos);
        bottomRight = Maths.rotate(bottomRight, sin, cos);
        topRight = Maths.rotate(topRight, sin, cos);
        topLeft = Maths.rotate(topLeft, sin, cos);
        return PointSet.Four.create(bottomLeft, bottomRight, topRight, topLeft);
    }

    protected final ImmutableList<Vector2d> points;
    protected final double width;
    protected final double height;
    protected final double radians;

    private Rectangle(PointSet.Four<Vector2d> points, double calculatedWidth, double calculatedLength,
            double calculatedRadians) {
        this.points = ImmutableList.copyOf(PointSets.getCollection(points));
        this.width = calculatedWidth;
        this.height = calculatedLength;
        this.radians = calculatedRadians;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    @Override
    public List<Vector2d> getPoints() {
        return this.points;
    }

    @Override
    public List<Vector2d> getPointsAtOffset(Vector2d offset) {
        return FluentIterable.from(this.points).transform(x -> x.add(offset)).toList();
    }

    @Override
    public Rectangle scale(Vector2d delta) {
        return fromLengthWidthAndRadians(this.width * delta.getX(), this.height * delta.getY(), this.radians);
    }

    @Override
    public Rectangle rotateZ(double radians) {
        return fromLengthWidthAndRadians(this.width, this.height, this.radians + radians);
    }

    @Override
    public Rectangle transform(ShapeTransform<Rectangle> transform) {
        return transform.apply(this);
    }

    @Override
    public Vector2d get1stPoint() {
        return this.points.get(0);
    }

    @Override
    public Vector2d get2ndPoint() {
        return this.points.get(1);
    }

    @Override
    public Vector2d get3rdPoint() {
        return this.points.get(2);
    }

    @Override
    public Vector2d get4thPoint() {
        return this.points.get(3);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Rectangle && equals((Rectangle) obj));
    }

    public boolean equals(Rectangle rect) {
        return (rect == this) || (Double.compare(this.width, rect.width) == 0
                && Double.compare(this.height, rect.height) == 0 && Double.compare(this.radians, rect.radians) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.width, this.height, this.radians);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("points", this.points).add("width", this.width)
                .add("height", this.height).add("radians", this.radians).toString();
    }

}
