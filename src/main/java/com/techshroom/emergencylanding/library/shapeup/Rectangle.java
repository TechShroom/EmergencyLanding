package com.techshroom.emergencylanding.library.shapeup;

import java.util.List;

import com.flowpowered.math.TrigMath;
import com.flowpowered.math.vector.Vector2d;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.techshroom.emergencylanding.library.shapeup.generated_please_ignore.PointSet;
import com.techshroom.emergencylanding.library.util.Maths;

public class Rectangle
        implements Shape.TwoDimensional<Rectangle>, PointSet.Four<Vector2d> {

    public static Rectangle fromLengthAndWidth(double width, double height) {
        return new Rectangle(
                PointSet.Four.create(Vector2d.ZERO, new Vector2d(width, 0),
                        new Vector2d(width, height), new Vector2d(0, height)),
                width, height, 0);
    }

    public static Rectangle fromLengthWidthAndRadians(double width,
            double height, double radians) {
        return new Rectangle(computeRotatedPoints(width, height, radians),
                width, height, radians);
    }

    private static PointSet.Four<Vector2d> computeRotatedPoints(double width,
            double height, double radians) {
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

    private Rectangle(PointSet.Four<Vector2d> points, double calculatedWidth,
            double calculatedLength, double calculatedRadians) {
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
        return FluentIterable.from(this.points).transform(x -> x.add(offset))
                .toList();
    }

    @Override
    public Rectangle scale(Vector2d delta) {
        return fromLengthWidthAndRadians(this.width * delta.getX(),
                this.height * delta.getY(), this.radians);
    }

    @Override
    public Rectangle rotateZ(double radians) {
        return fromLengthWidthAndRadians(this.width, this.height,
                this.radians + radians);
    }

    @Override
    public Rectangle transform(ShapeTransform<Rectangle> transform) {
        return transform.apply(this);
    }

    @Override
    public Vector2d get1stPoint() {
        return null;
    }

    @Override
    public Vector2d get2ndPoint() {
        return null;
    }

    @Override
    public Vector2d get3rdPoint() {
        return null;
    }

    @Override
    public Vector2d get4thPoint() {
        return null;
    }

}
