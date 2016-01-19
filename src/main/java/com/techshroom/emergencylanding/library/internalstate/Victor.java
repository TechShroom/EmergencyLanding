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
