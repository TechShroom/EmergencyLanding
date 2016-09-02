package com.techshroom.emergencylanding.library.internalstate;

import static com.google.common.base.Preconditions.checkArgument;

import com.techshroom.emergencylanding.library.exceptions.OutOfBoundsException;

public class QuadTree {

    private static final int TOP_LEFT = 0;
    private static final int TOP_RIGHT = 1;
    private static final int BOTTOM_LEFT = 2;
    private static final int BOTTOM_RIGHT = 3;

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final int leftXMax;
    private final int topYMax;
    private final int maxPreSplit;
    private QuadTree[] quadrants;
    private int preSplitInsertIndex = 0;
    private ELEntity[] preSplit;

    public QuadTree(int x1, int y1, int x2, int y2, int maxPreSplit) {
        checkArgument(x1 <= x2, "x1 must be the minimum");
        checkArgument(y1 <= y2, "y1 must be the minimum");
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.leftXMax = x1 + (x2 - x1) / 2;
        this.topYMax = y1 + (y2 - y1) / 2;
        this.maxPreSplit = maxPreSplit;
        this.preSplit = new ELEntity[maxPreSplit];
    }

    private boolean inLeft(double x) {
        return this.x1 <= x && x <= this.leftXMax;
    }

    private boolean inRight(double x) {
        return this.leftXMax < x && x <= this.x2;
    }

    private boolean inTop(double y) {
        return this.y1 <= y && y <= this.topYMax;
    }

    private boolean inBottom(double y) {
        return this.topYMax < y && y <= this.y2;
    }

    private boolean inTopLeft(double x, double y) {
        return inTop(y) && inLeft(x);
    }

    private boolean inTopRight(double x, double y) {
        return inTop(y) && inRight(x);
    }

    private boolean inBottomLeft(double x, double y) {
        return inBottom(y) && inLeft(x);
    }

    private boolean inBottomRight(double x, double y) {
        return inBottom(y) && inRight(x);
    }

    private int lowX(int quad) {
        switch (quad) {
            case TOP_LEFT:
            case BOTTOM_LEFT:
                return this.x1;
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
                return this.leftXMax + 1;
        }
        throw new IllegalArgumentException("bad quad: " + quad);
    }

    private int lowY(int quad) {
        switch (quad) {
            case TOP_LEFT:
            case TOP_RIGHT:
                return this.y1;
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                return this.topYMax + 1;
        }
        throw new IllegalArgumentException("bad quad: " + quad);
    }

    private int highX(int quad) {
        switch (quad) {
            case TOP_LEFT:
            case BOTTOM_LEFT:
                return this.leftXMax;
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
                return this.x2;
        }
        throw new IllegalArgumentException("bad quad: " + quad);
    }

    private int highY(int quad) {
        switch (quad) {
            case TOP_LEFT:
            case TOP_RIGHT:
                return this.topYMax;
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                return this.y2;
        }
        throw new IllegalArgumentException("bad quad: " + quad);
    }

    private int coordsToQuad(double x, double y) {
        if (inTopLeft(x, y)) {
            return TOP_LEFT;
        }
        if (inTopRight(x, y)) {
            return TOP_RIGHT;
        }
        if (inBottomLeft(x, y)) {
            return BOTTOM_LEFT;
        }
        assert inBottomRight(x, y);
        return BOTTOM_RIGHT;
    }

    private int itemToQuad(ELEntity entity) {
        return coordsToQuad(entity.getX(), entity.getY());
    }
    
    private boolean coordsOutOfBounds(double x, double y) {
        return this.x2 < x || x < this.x1 || this.y2 < y || y < this.y1;
    }
    
    private boolean itemOutOfBounds(ELEntity e) {
        return coordsOutOfBounds(e.getX(), e.getY());
    }

    private void insert(ELEntity entity) {
        if (this.preSplitInsertIndex == this.maxPreSplit) {
            // full, rewrite as quadrants
            reinsertWithQuads();
        } else {
            // insert without quads
            this.preSplit[this.preSplitInsertIndex] = entity;
            this.preSplitInsertIndex++;
        }
    }

    private void reinsertWithQuads() {
        // save + clear pre-split list
        ELEntity[] insert = this.preSplit;
        this.preSplit = null;

        // create quad array
        this.quadrants = new QuadTree[4];

        // insert each
        for (ELEntity e : insert) {
            int quad = itemToQuad(e);
            insertIntoQuad(e, quad);
        }
    }

    private void insertIntoQuad(ELEntity entity, int quad) {
        QuadTree tree = this.quadrants[quad];
        if (tree == null) {
            tree = this.quadrants[quad] =
                    new QuadTree(lowX(quad), lowY(quad), highX(quad), highY(quad), this.maxPreSplit);
        }
        tree.add(entity);
    }

    public void add(ELEntity entity) {
        if (itemOutOfBounds(entity)) {
          //  throw new OutOfBoundsException();
        }
        insert(entity);
    }

    public ELEntity find(double x, double y) {
        if (coordsOutOfBounds(x, y)) {
            return null;
        }
        if (this.preSplit != null) {
            for (ELEntity e : this.preSplit) {
                // TODO how should we compare doubles?
                // this might be "too accurate"
                if (Double.compare(e.getX(), x) == 0 && Double.compare(e.getY(), y) == 0) {
                       return e;
                }
            }
        } else {
            int quad = coordsToQuad(x, y);
            QuadTree tree = this.quadrants[quad];
            if (tree != null) {
                return tree.find(x, y);
            }
        }
        return null;
    }

}
