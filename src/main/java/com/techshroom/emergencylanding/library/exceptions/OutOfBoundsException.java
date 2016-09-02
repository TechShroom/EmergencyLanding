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
