/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <https://techshoom.com>
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
