package emergencylanding.k.library.util;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A 2D implementation of {@link Bounds}.
 * 
 * @author Kenzie Togami
 */
public class Bounds2D extends Bounds {

    private double minx, miny, maxx, maxy;

    // little cache that stores values until bounds change
    private transient Rectangle2D cache = null;

    /**
     * Convert this Bounds2D to a {@link Rectangle2D}. This operation is cached.
     * 
     * @return a {@link Rectangle2D} that represents this Bounds2D object
     */
    public Rectangle2D asRect() {
        return asRect(null);
    }

    /**
     * Convert this Bounds2D to a {@link Rectangle2D}. This operation is cached.
     * 
     * @param dest
     *            - the Rectangle2D to store the values into, Can be
     *            <code>null</code>, which creates a new Rectangle2D.
     * @return a {@link Rectangle2D} that represents this Bounds2D object
     */
    public Rectangle2D asRect(Rectangle2D dest) {
        if (dest == null) {
            dest = new Rectangle2D.Double();
        }
        if (cache == null) {
            cache = new Rectangle2D.Double(minx, miny, maxx - minx,
                    maxy - miny);
        }
        dest.setRect(cache);
        return dest;
    }

    /**
     * @return the maxx
     */
    public double getMaxX() {
        return maxx;
    }

    /**
     * @return the maxy
     */
    public double getMaxY() {
        return maxy;
    }

    /**
     * @return the minx
     */
    public double getMinX() {
        return minx;
    }

    /**
     * @return the miny
     */
    public double getMinY() {
        return miny;
    }

    @Override
    public boolean intersects(Bounds b) {
        if (b instanceof Bounds2D) {
            return intersects((Bounds2D) b);
        }
        return false;
    }

    public boolean intersects(Bounds2D box) {
        double x = box.minx, y = box.miny, x2 = box.maxx, y2 = box.maxy;
        if (isEmpty() || box.isEmpty()) {
            return false;
        }
        double x0 = minx, x1 = maxx;
        double y0 = miny, y1 = maxy;
        return (x2 >= x0 && y2 >= y0 && x <= x1 && y <= y1);
    }

    /**
     * Force a cache recalculation. This may be removed in a future release if
     * the cache is removed.
     */
    public void invalidateCache() {
        cache = null;
    }

    public boolean isEmpty() {
        return (maxx - minx) == 0 || (maxy - miny) == 0;
    }

    /**
     * Two points define a Bounds2D.
     */
    @Override
    public int pointCount() {
        return 2;
    }

    @Override
    public void setBounds(Point2D[] bounds) {
        if (bounds.length != pointCount()) {
            throw new IllegalArgumentException("requires 2 points only");
        }
        minx = bounds[0].getX();
        miny = bounds[0].getY();
        maxx = bounds[1].getX();
        maxy = bounds[1].getY();
        cache = null;
    }

    /**
     * @param maxx
     *            the maxx to set
     */
    public void setMaxX(double maxx) {
        this.maxx = maxx;
    }

    /**
     * @param maxy
     *            the maxy to set
     */
    public void setMaxY(double maxy) {
        this.maxy = maxy;
    }

    /**
     * @param minx
     *            the minx to set
     */
    public void setMinX(double minx) {
        this.minx = minx;
    }

    /**
     * @param miny
     *            the miny to set
     */
    public void setMinY(double miny) {
        this.miny = miny;
    }

    @Override
    public Point2D[] getBounds() {
        return new Point2D[] { new Point2D.Double(minx, miny),
                new Point2D.Double(maxx, maxy) };
    }
}