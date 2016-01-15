package emergencylanding.k.library.internalstate;

import emergencylanding.k.library.util.Maths;

public class Victor {

    protected static int state = 0;
    public float x, y, z;
    public float lastX, lastY, lastZ;
    public boolean init = false;

    public Victor() {
        this(0, 0, 0);
    }

    public Victor(float x, float y, float z) {
        init(x, y, z);
    }

    public Victor interpolate(float del) {
        Victor inter = new Victor();
        if (del > 1) {
            System.err.println("del > 1 (" + del + ")");
        }
        inter.x = Maths.lerp(lastX, x, del);
        inter.y = Maths.lerp(lastY, y, del);
        inter.z = Maths.lerp(lastZ, z, del);
        return inter;
    }

    public void update(float xVal, float yVal, float zVal) {
        lastX = x;
        lastY = y;
        lastZ = z;
        x = xVal;
        y = yVal;
        z = zVal;
    }

    public synchronized void init(float xVal, float yVal, float zVal) {
        if (init) {
            update(xVal, yVal, zVal);
            return;
        }
        x = xVal;
        y = yVal;
        z = zVal;
        update(xVal, yVal, zVal);
        init = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Victor) {
            Victor v = (Victor) o;
            /*
             * Note: possible want to consider comparing lastX/Y/Z, as that
             * makes calls to interpolate() different.
             */
            return v.x == x && v.y == y && v.z == z;
        }
        return super.equals(o);
    }

    public synchronized static void flip() {
        Victor.state = 1 - Victor.state;
    }
}
