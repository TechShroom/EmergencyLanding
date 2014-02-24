package emergencylanding.k.library.util;

public final class Maths {

    private Maths() {
        throw new AssertionError("Don't create");
    }

    /**
     * Finds the length of a line when projected along another line. Used for
     * collisions.
     * 
     * @param thetaSurface
     *            - The angle the surface to be projected upon makes with the
     *            horizontal
     * @param thetaLineToProject
     *            - The angle the line makes with the horizontal
     * @param magnitude
     *            - The length of the line.
     * @param perpendicular
     *            - Gets the length of the line when projected along the line
     *            perpendicular to the thetaSurface given (eg, find the y).
     */
    public static double projectLineAlongSurface(double thetaSurface,
            double thetaLineToProject, double magnitude, boolean perpendicular) {
        double theta = thetaLineToProject - thetaSurface;
        if (perpendicular) {
            theta += 90;
        }
        return Math.cos(Math.toRadians(theta)) * magnitude;
    }

}
