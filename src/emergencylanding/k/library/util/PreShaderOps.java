package emergencylanding.k.library.util;

import java.util.LinkedList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public final class PreShaderOps {

    private static final LinkedList<Matrix4f> applies =
            new LinkedList<Matrix4f>();

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
        vec.x = m.m00 * vx + m.m10 * vy + m.m20 * vz + m.m30;
        vec.y = m.m01 * vx + m.m11 * vy + m.m21 * vz + m.m31;
        vec.z = m.m02 * vx + m.m12 * vy + m.m22 * vz + m.m32;
    }
}
