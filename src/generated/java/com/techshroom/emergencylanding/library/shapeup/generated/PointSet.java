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
package com.techshroom.emergencylanding.library.shapeup.generated;

import com.flowpowered.math.vector.Vectord;
import com.google.common.collect.ImmutableList;
import java.util.Collection;

/**
 * A PointSet contains a certain amount of points, which can be accessed with {@link #getPointCount}.
 * There are 10 pre-generated interfaces that are for common points, and will return the correct number from {@code getPointCount}.
 * Automatically generated by PCG on Tue, 19 Apr 2016 16:00:47 GMT. All edits will be discarded when regenerated.
 */
public interface PointSet<V extends Vectord> {
    Collection<V> getPoints();

    int getPointCount();

    interface One<V extends Vectord> extends PointSet<V>, HasXPoint.One<V> {
        static <V extends Vectord> One<V> create(V point1) {
            ImmutableList<V> points = ImmutableList.of(point1);
            return new One<V>() {
                @Override
                public V get1stPoint() {
                    return point1;
                }

                @Override
                public ImmutableList<V> getPoints() {
                    return points;
                }
            };
        }

        @Override
        default int getPointCount() {
            return 1;
        }
    }

    interface Two<V extends Vectord> extends PointSet<V>, HasXPoint.Two<V> {
        static <V extends Vectord> Two<V> create(V point1, V point2) {
            ImmutableList<V> points = ImmutableList.of(point1, point2);
            return new Two<V>() {
                @Override
                public V get1stPoint() {
                    return point1;
                }

                @Override
                public V get2ndPoint() {
                    return point2;
                }

                @Override
                public ImmutableList<V> getPoints() {
                    return points;
                }
            };
        }

        @Override
        default int getPointCount() {
            return 2;
        }
    }

    interface Three<V extends Vectord> extends PointSet<V>, HasXPoint.Three<V> {
        static <V extends Vectord> Three<V> create(V point1, V point2, V point3) {
            ImmutableList<V> points = ImmutableList.of(point1, point2, point3);
            return new Three<V>() {
                @Override
                public V get1stPoint() {
                    return point1;
                }

                @Override
                public V get2ndPoint() {
                    return point2;
                }

                @Override
                public V get3rdPoint() {
                    return point3;
                }

                @Override
                public ImmutableList<V> getPoints() {
                    return points;
                }
            };
        }

        @Override
        default int getPointCount() {
            return 3;
        }
    }

    interface Four<V extends Vectord> extends PointSet<V>, HasXPoint.Four<V> {
        static <V extends Vectord> Four<V> create(V point1, V point2, V point3, V point4) {
            ImmutableList<V> points = ImmutableList.of(point1, point2, point3, point4);
            return new Four<V>() {
                @Override
                public V get1stPoint() {
                    return point1;
                }

                @Override
                public V get2ndPoint() {
                    return point2;
                }

                @Override
                public V get3rdPoint() {
                    return point3;
                }

                @Override
                public V get4thPoint() {
                    return point4;
                }

                @Override
                public ImmutableList<V> getPoints() {
                    return points;
                }
            };
        }

        @Override
        default int getPointCount() {
            return 4;
        }
    }

    interface Five<V extends Vectord> extends PointSet<V>, HasXPoint.Five<V> {
        static <V extends Vectord> Five<V> create(V point1, V point2, V point3, V point4, V point5) {
            ImmutableList<V> points = ImmutableList.of(point1, point2, point3, point4, point5);
            return new Five<V>() {
                @Override
                public V get1stPoint() {
                    return point1;
                }

                @Override
                public V get2ndPoint() {
                    return point2;
                }

                @Override
                public V get3rdPoint() {
                    return point3;
                }

                @Override
                public V get4thPoint() {
                    return point4;
                }

                @Override
                public V get5thPoint() {
                    return point5;
                }

                @Override
                public ImmutableList<V> getPoints() {
                    return points;
                }
            };
        }

        @Override
        default int getPointCount() {
            return 5;
        }
    }

    interface Six<V extends Vectord> extends PointSet<V>, HasXPoint.Six<V> {
        static <V extends Vectord> Six<V> create(V point1, V point2, V point3, V point4, V point5, V point6) {
            ImmutableList.Builder<V> pointsBuilder = ImmutableList.builder();
            pointsBuilder.add(point1);
            pointsBuilder.add(point2);
            pointsBuilder.add(point3);
            pointsBuilder.add(point4);
            pointsBuilder.add(point5);
            pointsBuilder.add(point6);
            ImmutableList<V> points = pointsBuilder.build();
            return new Six<V>() {
                @Override
                public V get1stPoint() {
                    return point1;
                }

                @Override
                public V get2ndPoint() {
                    return point2;
                }

                @Override
                public V get3rdPoint() {
                    return point3;
                }

                @Override
                public V get4thPoint() {
                    return point4;
                }

                @Override
                public V get5thPoint() {
                    return point5;
                }

                @Override
                public V get6thPoint() {
                    return point6;
                }

                @Override
                public ImmutableList<V> getPoints() {
                    return points;
                }
            };
        }

        @Override
        default int getPointCount() {
            return 6;
        }
    }

    interface Seven<V extends Vectord> extends PointSet<V>, HasXPoint.Seven<V> {
        static <V extends Vectord> Seven<V> create(V point1, V point2, V point3, V point4, V point5, V point6, V point7) {
            ImmutableList.Builder<V> pointsBuilder = ImmutableList.builder();
            pointsBuilder.add(point1);
            pointsBuilder.add(point2);
            pointsBuilder.add(point3);
            pointsBuilder.add(point4);
            pointsBuilder.add(point5);
            pointsBuilder.add(point6);
            pointsBuilder.add(point7);
            ImmutableList<V> points = pointsBuilder.build();
            return new Seven<V>() {
                @Override
                public V get1stPoint() {
                    return point1;
                }

                @Override
                public V get2ndPoint() {
                    return point2;
                }

                @Override
                public V get3rdPoint() {
                    return point3;
                }

                @Override
                public V get4thPoint() {
                    return point4;
                }

                @Override
                public V get5thPoint() {
                    return point5;
                }

                @Override
                public V get6thPoint() {
                    return point6;
                }

                @Override
                public V get7thPoint() {
                    return point7;
                }

                @Override
                public ImmutableList<V> getPoints() {
                    return points;
                }
            };
        }

        @Override
        default int getPointCount() {
            return 7;
        }
    }

    interface Eight<V extends Vectord> extends PointSet<V>, HasXPoint.Eight<V> {
        static <V extends Vectord> Eight<V> create(V point1, V point2, V point3, V point4, V point5, V point6, V point7, V point8) {
            ImmutableList.Builder<V> pointsBuilder = ImmutableList.builder();
            pointsBuilder.add(point1);
            pointsBuilder.add(point2);
            pointsBuilder.add(point3);
            pointsBuilder.add(point4);
            pointsBuilder.add(point5);
            pointsBuilder.add(point6);
            pointsBuilder.add(point7);
            pointsBuilder.add(point8);
            ImmutableList<V> points = pointsBuilder.build();
            return new Eight<V>() {
                @Override
                public V get1stPoint() {
                    return point1;
                }

                @Override
                public V get2ndPoint() {
                    return point2;
                }

                @Override
                public V get3rdPoint() {
                    return point3;
                }

                @Override
                public V get4thPoint() {
                    return point4;
                }

                @Override
                public V get5thPoint() {
                    return point5;
                }

                @Override
                public V get6thPoint() {
                    return point6;
                }

                @Override
                public V get7thPoint() {
                    return point7;
                }

                @Override
                public V get8thPoint() {
                    return point8;
                }

                @Override
                public ImmutableList<V> getPoints() {
                    return points;
                }
            };
        }

        @Override
        default int getPointCount() {
            return 8;
        }
    }

    interface Nine<V extends Vectord> extends PointSet<V>, HasXPoint.Nine<V> {
        static <V extends Vectord> Nine<V> create(V point1, V point2, V point3, V point4, V point5, V point6, V point7, V point8, V point9) {
            ImmutableList.Builder<V> pointsBuilder = ImmutableList.builder();
            pointsBuilder.add(point1);
            pointsBuilder.add(point2);
            pointsBuilder.add(point3);
            pointsBuilder.add(point4);
            pointsBuilder.add(point5);
            pointsBuilder.add(point6);
            pointsBuilder.add(point7);
            pointsBuilder.add(point8);
            pointsBuilder.add(point9);
            ImmutableList<V> points = pointsBuilder.build();
            return new Nine<V>() {
                @Override
                public V get1stPoint() {
                    return point1;
                }

                @Override
                public V get2ndPoint() {
                    return point2;
                }

                @Override
                public V get3rdPoint() {
                    return point3;
                }

                @Override
                public V get4thPoint() {
                    return point4;
                }

                @Override
                public V get5thPoint() {
                    return point5;
                }

                @Override
                public V get6thPoint() {
                    return point6;
                }

                @Override
                public V get7thPoint() {
                    return point7;
                }

                @Override
                public V get8thPoint() {
                    return point8;
                }

                @Override
                public V get9thPoint() {
                    return point9;
                }

                @Override
                public ImmutableList<V> getPoints() {
                    return points;
                }
            };
        }

        @Override
        default int getPointCount() {
            return 9;
        }
    }

    interface Ten<V extends Vectord> extends PointSet<V>, HasXPoint.Ten<V> {
        static <V extends Vectord> Ten<V> create(V point1, V point2, V point3, V point4, V point5, V point6, V point7, V point8, V point9, V point10) {
            ImmutableList.Builder<V> pointsBuilder = ImmutableList.builder();
            pointsBuilder.add(point1);
            pointsBuilder.add(point2);
            pointsBuilder.add(point3);
            pointsBuilder.add(point4);
            pointsBuilder.add(point5);
            pointsBuilder.add(point6);
            pointsBuilder.add(point7);
            pointsBuilder.add(point8);
            pointsBuilder.add(point9);
            pointsBuilder.add(point10);
            ImmutableList<V> points = pointsBuilder.build();
            return new Ten<V>() {
                @Override
                public V get1stPoint() {
                    return point1;
                }

                @Override
                public V get2ndPoint() {
                    return point2;
                }

                @Override
                public V get3rdPoint() {
                    return point3;
                }

                @Override
                public V get4thPoint() {
                    return point4;
                }

                @Override
                public V get5thPoint() {
                    return point5;
                }

                @Override
                public V get6thPoint() {
                    return point6;
                }

                @Override
                public V get7thPoint() {
                    return point7;
                }

                @Override
                public V get8thPoint() {
                    return point8;
                }

                @Override
                public V get9thPoint() {
                    return point9;
                }

                @Override
                public V get10thPoint() {
                    return point10;
                }

                @Override
                public ImmutableList<V> getPoints() {
                    return points;
                }
            };
        }

        @Override
        default int getPointCount() {
            return 10;
        }
    }
}
