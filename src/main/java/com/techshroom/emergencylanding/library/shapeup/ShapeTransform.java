package com.techshroom.emergencylanding.library.shapeup;

import java.util.function.Function;

public interface ShapeTransform<T extends Shape<T, ?>> extends Function<T, T> {

    static <S extends Shape<S, ?>> ShapeTransform<S> identity() {
        return s -> s;
    }

    @Override
    default T apply(T t) {
        return transform(t);
    }

    T transform(T shape);

}
