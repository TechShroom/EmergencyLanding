package com.techshroom.emergencylanding.library.lwjgl;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;

import org.lwjgl.openal.AL10;

import com.techshroom.emergencylanding.library.exceptions.lwjgl.OpenGLException;

public final class ErrUtil {

    public static void checkALError() {
        int err = AL10.alGetError();
        if (err != AL10.AL_NO_ERROR) {
            throw new RuntimeException(AL10.alGetString(err));
        }
    }

    public static void throwOGLIfError() throws OpenGLException {
        int err = glGetError();
        if (err != GL_NO_ERROR) {
            throw new OpenGLException(err);
        }
    }

    private ErrUtil() {
    }

}
