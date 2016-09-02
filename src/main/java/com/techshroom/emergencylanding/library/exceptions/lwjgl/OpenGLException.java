package com.techshroom.emergencylanding.library.exceptions.lwjgl;

import static org.lwjgl.opengl.ARBImaging.GL_TABLE_TOO_LARGE;
import static org.lwjgl.opengl.GL11.GL_INVALID_ENUM;
import static org.lwjgl.opengl.GL11.GL_INVALID_OPERATION;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;
import static org.lwjgl.opengl.GL11.GL_STACK_OVERFLOW;
import static org.lwjgl.opengl.GL11.GL_STACK_UNDERFLOW;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;

public class OpenGLException extends RuntimeException {

    private static final long serialVersionUID = 5809474683082305049L;

    public OpenGLException(int gl_error_code) {
        super(createErrorMessage(gl_error_code));
    }

    public static String translateGLErrorString(int errorCode) {
        switch (errorCode) {
            case GL_NO_ERROR:
                return "No error";
            case GL_INVALID_ENUM:
                return "Invalid enum";
            case GL_INVALID_VALUE:
                return "Invalid value";
            case GL_INVALID_OPERATION:
                return "Invalid operation";
            case GL_STACK_OVERFLOW:
                return "Stack overflow";
            case GL_STACK_UNDERFLOW:
                return "Stack underflow";
            case GL_OUT_OF_MEMORY:
                return "Out of memory";
            case GL_TABLE_TOO_LARGE:
                return "Table too large";
            case GL_INVALID_FRAMEBUFFER_OPERATION:
                return "Invalid framebuffer operation";
            default:
                return "Unknown";
        }
    }

    private static String createErrorMessage(int glErrCode) {
        String errString = translateGLErrorString(glErrCode);
        return errString + " (" + glErrCode + ")";
    }

}
