package emergencylanding.k.library.util;

import org.lwjgl.util.vector.Matrix4f;

final class GLTranslator {

    private GLTranslator() {
    }

    static void glBeginTrans(double ix, double iy, double iz) {
        Matrix4f mats = mats(ix, iy, iz);
        PreShaderOps.add(mats);
    }

    private static Matrix4f mats(double ix, double iy, double iz ) {
        return Maths.createTransMatrix(ix, iy, iz);
    }

    static void glEndTrans() {
        PreShaderOps.remove(1);
    }
}
