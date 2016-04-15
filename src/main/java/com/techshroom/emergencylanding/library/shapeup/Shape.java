package com.techshroom.emergencylanding.library.shapeup;

import java.util.List;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vectord;

public interface Shape<T extends Shape<T, P>, P extends Vectord> {

    interface TwoDimensional<T extends TwoDimensional<T>>
            extends Shape<T, Vector2d> {

    }

    interface ThreeDimensional<T extends ThreeDimensional<T>>
            extends Shape<T, Vector3d> {

        T rotateX(double radians);

        T rotateY(double radians);

    }

    List<P> getPoints();

    List<P> getPointsAtOffset(P offset);

    // No position data
    // T move(P delta);

    T scale(P delta);

    T rotateZ(double radians);

    T transform(ShapeTransform<T> transform);

}
