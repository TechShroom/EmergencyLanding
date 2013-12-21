package emergencylanding.k.library.internalstate;

import emergencylanding.k.library.util.DrawableUtils;

public class Victor {
    protected static int state = 0;
    public float x, y, z;
    public float lastX, lastY, lastZ;

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
	inter.x = DrawableUtils.lerp(lastX, x, del);
	inter.y = DrawableUtils.lerp(lastY, y, del);
	inter.z = DrawableUtils.lerp(lastZ, z, del);
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

    public void init(float xVal, float yVal, float zVal) {
	x = xVal;
	y = yVal;
	z = zVal;
	update(xVal, yVal, zVal);
    }

    public synchronized static void flip() {
	Victor.state = 1 - Victor.state;
    }
}
