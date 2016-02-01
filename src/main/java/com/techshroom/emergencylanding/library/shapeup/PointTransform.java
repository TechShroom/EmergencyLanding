package com.techshroom.emergencylanding.library.shapeup;

import java.util.function.Function;

import com.flowpowered.math.vector.Vector3d;

/**
 * Interface for something that can change a single point to another point.
 */
public interface PointTransform extends Function<Vector3d, Vector3d> {

    PointTransform IDENTITY = v -> v;

    @Override
    default Vector3d apply(Vector3d t) {
        return transform(t);
    }

    Vector3d transform(Vector3d vec);

}
