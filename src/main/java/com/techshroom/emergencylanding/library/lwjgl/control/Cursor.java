package com.techshroom.emergencylanding.library.lwjgl.control;

import org.lwjgl.glfw.GLFW;

import com.google.auto.value.AutoValue;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;

@AutoValue
public abstract class Cursor {

    private static final Cursor NORMAL_CURSOR =
            fromHandle(GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR));

    public static Cursor getStandardCursor(int cursorType) {
        if (cursorType == GLFW.GLFW_ARROW_CURSOR) {
            return NORMAL_CURSOR;
        }
        return fromHandle(GLFW.glfwCreateStandardCursor(cursorType));
    }

    public static Cursor createCursor(ELTexture image, int hotX, int hotY) {
        return fromHandle(
                GLFW.glfwCreateCursor(image.convertToGLFWImage(), hotX, hotY));
    }

    public static Cursor fromHandle(long handle) {
        return new AutoValue_Cursor(handle);
    }

    Cursor() {
    }

    public abstract long getHandle();

    public final void useOnWindow(long window) {
        GLFW.glfwSetCursor(window, getHandle());
    }

}
