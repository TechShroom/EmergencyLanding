package com.techshroom.emergencylanding.library.internalstate;

import com.techshroom.emergencylanding.library.util.Maths;

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
        inter.x = Maths.lerp(this.lastX, this.x, del);
        inter.y = Maths.lerp(this.lastY, this.y, del);
        inter.z = Maths.lerp(this.lastZ, this.z, del);
        return inter;
    }

    public void update(float xVal, float yVal, float zVal) {
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;
        this.x = xVal;
        this.y = yVal;
        this.z = zVal;
    }

    public synchronized void init(float xVal, float yVal, float zVal) {
        if (this.init) {
            update(xVal, yVal, zVal);
            return;
        }
        this.x = xVal;
        this.y = yVal;
        this.z = zVal;
        update(xVal, yVal, zVal);
        this.init = true;
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
            return v.x == this.x && v.y == this.y && v.z == this.z;
        }
        return super.equals(o);
    }

    public synchronized static void flip() {
        Victor.state = 1 - Victor.state;
    }
}
