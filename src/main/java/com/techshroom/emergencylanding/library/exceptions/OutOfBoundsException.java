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
package com.techshroom.emergencylanding.library.exceptions;

public class OutOfBoundsException extends RuntimeException {

    private static final long serialVersionUID = -8149433464998317064L;

    private static String
            createMessage(double providedX, double providedY, double minX, double minY, double maxX, double maxY) {
        if (minX <= providedX && providedX <= maxX) {
            // only Y out of bounds
            return String.format("bounds: %s <= y <= %s; given: %s");
        }
        if (minY <= providedY && providedY <= maxY) {
            // only X out of bounds
            return String.format("bounds: %s <= x <= %s; given: %s");
        }
        return null;
    }

    private final double providedX;
    private final double providedY;
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    private OutOfBoundsException(double providedX, double providedY, double minX, double minY, double maxX,
            double maxY) {
        super(createMessage(providedX, providedY, minX, minY, maxX, maxY));
        this.providedX = providedX;
        this.providedY = providedY;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

}
