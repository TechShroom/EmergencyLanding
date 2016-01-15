package emergencylanding.k.library.util;

import java.awt.geom.Point2D;

//TODO: Flesh out
public class Bounds3D extends Bounds {

    private double minx, miny, minz, maxx, maxy, maxz;

    /**
     * <p>
     * NB: Because there is no Point3D class so we can unify everything,
     * <strong> the third point's x defines minz and it's y defines
     * maxz.</strong>
     * </p>
     * {@inheritDoc}
     */
    @Override
    public void setBounds(Point2D[] bounds) {
        if (bounds.length != pointCount()) {
            throw new IllegalArgumentException("requires 3 points only");
        }
        minx = bounds[0].getX();
        miny = bounds[0].getY();
        maxx = bounds[1].getX();
        maxy = bounds[1].getY();
        minz = bounds[2].getX();
        maxz = bounds[2].getY();
    }

    /**
     * Three points define a Bounds3D.
     */
    @Override
    public int pointCount() {
        return 3;
    }

    @Override
    public boolean intersects(Bounds b) {
        throw new UnsupportedOperationException();
    }

    /**
     * <p>
     * NB: Because there is no Point3D class so we can unify everything,
     * <strong> the third point's x defines minz and it's y defines
     * maxz.</strong>
     * </p>
     * {@inheritDoc}
     */
    @Override
    public Point2D[] getBounds() {
        return new Point2D[] { new Point2D.Double(minx, miny),
                new Point2D.Double(maxx, maxy),
                new Point2D.Double(minz, maxz) };
    }
}