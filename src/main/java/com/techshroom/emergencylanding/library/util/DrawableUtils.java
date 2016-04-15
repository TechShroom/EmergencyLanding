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
package com.techshroom.emergencylanding.library.util;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import com.flowpowered.math.vector.Vector2i;
import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.tex.BufferedTexture;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;

public final class DrawableUtils {

    /**
     * Chops a buffer to the specified width, height, and possibly an extra
     * dimension.
     * 
     * The returned buffer is rewinded.
     * 
     * @param buf
     *            - the buffer to shrink
     * @param oldWidthHeight
     *            - the old width and height (should multiply with
     *            extraDimensions to equal buf.capacity())
     * @param choppedWidth
     *            - the new (smaller) width
     * @param choppedHeight
     *            - the new (smaller) height
     * @param extraDimensions
     *            - any extra dimensions, like bytes of color that should be
     *            kept together. Should be 1 if there is none.
     * @return the chopped buffer
     */
    public static ByteBuffer chopBuffer(ByteBuffer buf, Vector2i oldWidthHeight,
            int choppedWidth, int choppedHeight, int extraDimensions) {
        if (buf.capacity() < choppedWidth * choppedHeight * extraDimensions
                || oldWidthHeight.getX() < choppedHeight
                || oldWidthHeight.getY() < choppedWidth
                || extraDimensions < 1) {
            throw new IndexOutOfBoundsException(
                    "One of the dimensions is breaking the bounds of the buffer!");
        }
        if (buf.capacity() > buf.remaining()) {
            buf.rewind();
        }
        ByteBuffer newBuf = ByteBuffer
                .allocateDirect(choppedWidth * choppedHeight * extraDimensions);
        byte[][][] arrayOfStuff =
                new byte[choppedWidth][choppedHeight][extraDimensions];
        for (int i = 0; i < oldWidthHeight.getX() * oldWidthHeight.getY()
                * extraDimensions; i += extraDimensions) {
            int x = i / extraDimensions / oldWidthHeight.getX();
            int y = i / extraDimensions % oldWidthHeight.getY();
            LUtils.print("x/y=" + String.format("%s/%s", x, y));
            if (x >= choppedWidth || y >= choppedHeight) {
                continue;
            }
            for (int extra = 0; extra < extraDimensions; extra++) {
                arrayOfStuff[x][y][extra] = buf.get(i + extra);
            }
        }
        for (byte[][] b2d : arrayOfStuff) {
            for (byte[] b1d : b2d) {
                for (byte b : b1d) {
                    newBuf.put(b);
                }
            }
        }
        newBuf.rewind();
        return newBuf;
    }

    // Nope. We'll use stb if we need it.

    // public static BufferedImage getFontImage(char ch, boolean antiAlias,
    // Font font, int fontSize) {
    // return getFontImage(ch, antiAlias, font, fontSize, Color.WHITE);
    // }
    //
    // private static final int charx = 3, chary = 1;
    //
    // public static BufferedImage getFontImage(char ch, boolean antiAlias,
    // Font font, int fontSize, Color c) {
    // // Create a temporary image to extract the character's size
    // BufferedImage tempfontImage =
    // new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    // Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
    // if (antiAlias == true) {
    // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    // RenderingHints.VALUE_ANTIALIAS_ON);
    // }
    // g.setFont(font);
    // FontMetrics fontMetrics = g.getFontMetrics();
    // int charwidth = fontMetrics.charWidth(ch) + 8;
    //
    // if (charwidth <= 0) {
    // charwidth = 7;
    // }
    // int charheight = fontMetrics.getHeight() + 3;
    // if (charheight <= 0) {
    // charheight = fontSize;
    // }
    //
    // // Create another image holding the character we are creating
    // BufferedImage fontImage = new BufferedImage(charwidth, charheight,
    // BufferedImage.TYPE_INT_ARGB);
    //
    // Graphics2D gt = (Graphics2D) fontImage.getGraphics();
    //
    // if (antiAlias == true) {
    // gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    // RenderingHints.VALUE_ANTIALIAS_ON);
    // }
    //
    // gt.setFont(font);
    // gt.setColor(c);
    // gt.drawString(String.valueOf(ch), (charx),
    // (chary) + fontMetrics.getAscent());
    //
    // return fontImage;
    // }

    public static ELTexture getTextureFromFile(String file) {
        ELTexture t = BufferedTexture.generateTexture("/",
                file.replace('/', File.separatorChar));
        System.err.println("Loaded " + file);
        return t;
    }

    public static void glBeginRot(double theta, double rx, double ry,
            double rz) {
        GLRotator.glBeginRot(theta, rx, ry, rz);
    }

    public static void glEndRot() {
        GLRotator.glEndRot();
    }

    public static void glBeginTrans(double ix, double iy, double iz) {
        GLTranslator.glBeginTrans(ix, iy, iz);
    }

    public static void glEndTrans() {
        GLTranslator.glEndTrans();
    }

    /**
     * WARNING: Scaling may not be work in the opposite direction, as we use 1
     * over the scale values which may not be the direct inverse.
     * 
     * @param sx
     * @param sy
     * @param sz
     */
    public static void glBeginScale(double sx, double sy, double sz) {
        GLScaler.glBeginScale(sx, sy, sz);
    }

    /**
     * WARNING: Scaling may not be work in the opposite direction, as we use 1
     * over the scale values which may not be the direct inverse.
     * 
     * @param sx
     * @param sy
     * @param sz
     */
    public static void glEndScale() {
        GLScaler.glEndScale();
    }

    public static void beginStandardEntityRender(ELEntity entity, float posX,
            float posY, float posZ) {
        DrawableUtils.glBeginTrans(posX, posY, posZ);
        DrawableUtils.glBeginRot(entity.getYaw(), 0, 1, 0);
        DrawableUtils.glBeginRot(entity.getPitch(), 0, 0, 1);
        DrawableUtils.glBeginRot(entity.getRoll(), 1, 0, 0);
        DrawableUtils.glBeginTrans(-(entity.getTex().getWidth() / 2),
                -(entity.getTex().getHeight() / 2), 0);
    }

    public static int getWindowWidth() {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(GLFW.glfwGetCurrentContext(), width, null);
        return width.get(0);
    }

    public static int getWindowHeight() {
        IntBuffer height = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(GLFW.glfwGetCurrentContext(), null, height);
        return height.get(0);
    }

    public static Vector2i getWindowSize() {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(GLFW.glfwGetCurrentContext(), width, height);
        return new Vector2i(width.get(0), height.get(0));
    }

    public static Vector2i getWindowSize(DisplayLayer layer) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(layer.getWindow(), width, height);
        return new Vector2i(width.get(0), height.get(0));
    }

    public static void endStandardEntityRender() {
        DrawableUtils.glEndTrans();
        DrawableUtils.glEndRot();
        DrawableUtils.glEndRot();
        DrawableUtils.glEndRot();
        DrawableUtils.glEndTrans();
    }
}
