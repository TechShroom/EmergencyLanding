package com.techshroom.emergencylanding.library.shapeup;

import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;

import com.flowpowered.math.vector.Vectord;
import com.techshroom.emergencylanding.library.shapeup.generated_please_ignore.PointSet;

public final class PointSets {

    public static <V extends Vectord> Collection<V>
            getCollection(PointSet<V> set) {
        Collection<V> ret = set.getPoints();
        checkState(set.getPointCount() == ret.size(),
                "A PointSet must return a collection of size equal to what it claims.");
        return ret;
    }

    private PointSets() {
    }

}
