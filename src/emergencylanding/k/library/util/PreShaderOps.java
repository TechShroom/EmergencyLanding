package emergencylanding.k.library.util;

import java.util.*;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public final class PreShaderOps {
    private static final LinkedList<Matrix4f> applies = new LinkedList<Matrix4f>();

    private PreShaderOps() {
        throw new AssertionError("Nope.");
    }

    public static void add(Matrix4f... in) {
        for (Matrix4f m : in) {
            applies.add(m);
        }
    }

    public static void remove(int count) {
        for (; count > 0; count--) {
            applies.removeLast();
        }
    }

    public static void apply(Vector3f... in) {
        LinkedList<Matrix4f> c = new LinkedList<Matrix4f>(applies);
        Matrix4f m = new Matrix4f();
        int index = 0;
        if (c.size() >= 1) {
            for (Matrix4f mf : c) {
                if (mf == null) {
                    System.err.println("Null matrix");
                    applies.remove(index);
                    continue;
                }
                Matrix4f.mul(m, mf, m);
                index++;
            }
        }
        for (Vector3f v : in) {
            apply(v, m);
        }
    }

    /**
     * Applies the matrix to the vector in place.
     * 
     * @param vec
     *            - vector
     * @param m
     *            - matrix to multiply vector by
     */
    private static void apply(Vector3f vec, Matrix4f m) {
        float vx = vec.x, vy = vec.y, vz = vec.z;
        vec.x = m.m00 * vx + m.m01 * vy + m.m02 * vz + m.m03;
        vec.y = m.m10 * vx + m.m11 * vy + m.m12 * vz + m.m13;
        vec.z = m.m20 * vx + m.m21 * vy + m.m22 * vz + m.m23;
    }
}
