package emergencylanding.k.library.util;

import static org.lwjgl.opengl.GL11.*;

import java.util.LinkedList;

import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.util.vector.Matrix4f;

import emergencylanding.k.library.lwjgl.render.GLData;

final class GLTranslator {
    private static LinkedList<GLTranslator> transes = new LinkedList<GLTranslator>();
    private double iix, iiy, iiz; // inverse values

    private GLTranslator() {
    }

    static void glBeginTrans(double ix, double iy, double iz) {
        GLTranslator trans = new GLTranslator();
        trans.iix = -ix;
        trans.iiy = -iy;
        trans.iiz = -iz;
        Matrix4f mats = trans.mats(false);
        Matrix4f input = GLData.getMatrixToApply();
        Matrix4f.mul(input, mats, input);
        GLData.apply(input);
        transes.add(trans);
    }

    private Matrix4f mats(boolean inv) {
        double ix = iix, iy = iiy, iz = iiz;
        if (!inv) {
            ix = -ix;
            iy = -iy;
            iz = -iz;
        }
        return Maths.createTransMatrix(ix, iy, iz);
    }

    static void glEndTrans() {
        if (transes.isEmpty()) {
            throw new OpenGLException(GL_INVALID_OPERATION);
        }
        GLTranslator trans = transes.pollLast();
        Matrix4f mats = trans.mats(true);
        Matrix4f input = GLData.getMatrixToApply();
        Matrix4f.mul(input, mats, input);
        GLData.apply(input);
        GLData.apply(input);
    }
}
