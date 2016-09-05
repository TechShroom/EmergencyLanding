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
package com.techshroom.emergencylanding.library.lwjgl.render;

import java.nio.FloatBuffer;

import javax.annotation.Nullable;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import com.flowpowered.math.vector.Vector3f;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.util.PreShaderOps;

public class VertexData implements Cloneable {

    public static final int FLOAT_SIZE = 4;
    public static final int FLOATS_PER_POSTITON = 4;
    public static final int FLOATS_PER_COLOR = 4;
    public static final int FLOATS_PER_TEXTURE = 2;
    public static final int POSITION_SIZE = FLOATS_PER_POSTITON * FLOAT_SIZE;
    public static final int COLOR_SIZE = FLOATS_PER_COLOR * FLOAT_SIZE;
    public static final int TEXTURE_SIZE = FLOATS_PER_TEXTURE * FLOAT_SIZE;
    public static final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE + TEXTURE_SIZE;

    public static final int NO_DATA = -1, COLOR_FIRST = 0, COORDS_FIRST = 1, TEX_FIRST = 2;

    /**
     * Default vertex is origin
     */
    public final float[] verts = { 0f, 0f, 0f, 1f };

    /**
     * Default color is white
     */
    public final float[] colors = { 1f, 1f, 1f, 1f };

    /**
     * Default texture coords -1,0 (disabled)
     */
    public final float[] texCoords = { -1f, -1f };

    private int order = NO_DATA;

    public VertexData() {
        this(NO_DATA, new float[0]);
    }

    /**
     * Sets the data based on the given floats and the order. <br>
     * <br>
     * <table class="tg-table-plain">
     * <tr>
     * <th># of floats</th>
     * <th>Usage</th>
     * </tr>
     * <tr class="tg-even">
     * <td>3</td>
     * <td>XYZ</td>
     * </tr>
     * <tr>
     * <td>4</td>
     * <td>XYZW</td>
     * </tr>
     * <tr class="tg-even">
     * <td>5</td>
     * <td>XYZ-UV</td>
     * </tr>
     * <tr>
     * <td>6 and order 0/1</td>
     * <td>XYZ-RGB</td>
     * </tr>
     * <tr class="tg-even">
     * <td>6 and order 2</td>
     * <td>XYZW-UV</td>
     * </tr>
     * <tr>
     * <td>7 and order 0</td>
     * <td>XYZ-RGBA</td>
     * </tr>
     * <tr class="tg-even">
     * <td>7 and order 1</td>
     * <td>XYZW-RGB</td>
     * </tr>
     * <tr>
     * <td>8 and order 0/1</td>
     * <td>XYZW-RGBA</td>
     * </tr>
     * <tr class="tg-even">
     * <td>8 and order 2</td>
     * <td>XYZ-RGB-UV</td>
     * </tr>
     * <tr>
     * <td>9 and order 0</td>
     * <td>XYZ-RGBA-UV</td>
     * </tr>
     * <tr class="tg-even">
     * <td>9 and order 1</td>
     * <td>XYZW-RGB-UV</td>
     * </tr>
     * <tr>
     * <td>10</td>
     * <td>XYZW-RGBA-UV</td>
     * </tr>
     * <tr class="tg-even">
     * <td></td>
     * <td></td>
     * </tr>
     * <tr>
     * <td></td>
     * <td></td>
     * </tr>
     * </table>
     * 
     * @param order
     *            - the order to use the floats. 0 means go for color first, 1
     *            means go for coords first, and 3 means go for textures first
     * @param floats
     *            - the floats to use
     */
    public VertexData(int order, float... floats) {
        float x = 0f, y = 0f, z = 0f, w = 1f, r = 1f, g = 1f, b = 1f, a = 1f, u = -1f, v = 0f;
        switch (floats.length) {
            case 0:
                break;
            case 3:
                x = floats[0];
                y = floats[1];
                z = floats[2];
                break;
            case 4:
                x = floats[0];
                y = floats[1];
                z = floats[2];
                w = floats[3];
                break;
            case 5:
                x = floats[0];
                y = floats[1];
                z = floats[2];
                u = floats[3];
                v = floats[4];
                break;
            case 6:
                if (order == 2) {
                    x = floats[0];
                    y = floats[1];
                    z = floats[2];
                    w = floats[3];
                    u = floats[4];
                    v = floats[5];
                } else {
                    x = floats[0];
                    y = floats[1];
                    z = floats[2];
                    r = floats[3];
                    g = floats[4];
                    b = floats[5];
                }
                break;
            case 7:
                if (order == 0) {
                    x = floats[0];
                    y = floats[1];
                    z = floats[2];
                    r = floats[3];
                    g = floats[4];
                    b = floats[5];
                    a = floats[6];
                } else {
                    x = floats[0];
                    y = floats[1];
                    z = floats[2];
                    w = floats[3];
                    r = floats[4];
                    g = floats[5];
                    b = floats[6];
                }
                break;
            case 8:
                if (order == 2) {
                    x = floats[0];
                    y = floats[1];
                    z = floats[2];
                    r = floats[3];
                    g = floats[4];
                    b = floats[5];
                    u = floats[6];
                    v = floats[7];
                } else {
                    x = floats[0];
                    y = floats[1];
                    z = floats[2];
                    w = floats[3];
                    r = floats[4];
                    g = floats[5];
                    b = floats[6];
                    a = floats[7];
                }
                break;
            case 9:
                if (order == 0) {
                    x = floats[0];
                    y = floats[1];
                    z = floats[2];
                    r = floats[3];
                    g = floats[4];
                    b = floats[5];
                    a = floats[6];
                    u = floats[7];
                    v = floats[8];
                } else {
                    x = floats[0];
                    y = floats[1];
                    z = floats[2];
                    w = floats[3];
                    r = floats[4];
                    g = floats[5];
                    b = floats[6];
                    u = floats[7];
                    v = floats[8];
                }
                break;
            case 10:
                x = floats[0];
                y = floats[1];
                z = floats[2];
                w = floats[3];
                r = floats[4];
                g = floats[5];
                b = floats[6];
                a = floats[7];
                u = floats[8];
                v = floats[9];
                break;
        }
        setXYZW(x, y, z, w);
        setRGBA(r, g, b, a);
        setUV(u, v);
        this.order = order;
    }

    /**
     * Sets the x, y, and z
     * 
     * @param x
     *            - x
     * @param y
     *            - y
     * @param z
     *            - z
     * @return this
     */
    public VertexData setXYZ(float x, float y, float z) {
        return setXYZW(x, y, z, 1f);
    }

    /**
     * Sets the x, y, z, and w
     * 
     * @param x
     *            - x
     * @param y
     *            - y
     * @param z
     *            - z
     * @param w
     *            - w
     * @return this
     */
    public VertexData setXYZW(float x, float y, float z, float w) {
        this.verts[0] = x;
        this.verts[1] = y;
        this.verts[2] = z;
        this.verts[3] = w;
        return this;
    }

    /**
     * Sets the r, g, and b
     * 
     * @param r
     *            - red
     * @param g
     *            - green
     * @param b
     *            - blue
     * @return this
     */
    public VertexData setRGB(float r, float g, float b) {
        return setRGBA(r, g, b, 1f);
    }

    /**
     * Sets the r, g, b, and a
     * 
     * @param r
     *            - red
     * @param g
     *            - green
     * @param b
     *            - blue
     * @param a
     *            - alpha
     * @return this
     */
    public VertexData setRGBA(float r, float g, float b, float a) {
        this.colors[0] = r;
        this.colors[1] = g;
        this.colors[2] = b;
        this.colors[3] = a;
        return this;
    }

    /**
     * Sets the u and v
     * 
     * @param u
     *            - texture coord u
     * @param v
     *            - texture coord v
     * @return this
     */
    public VertexData setUV(float u, float v) {
        this.texCoords[0] = u;
        this.texCoords[1] = v;
        return this;
    }

    /**
     * Returns the order used to generate the vertex data
     * 
     * @return the order used to generate the vertex data
     */
    public int getOrderUsed() {
        return this.order;
    }

    @Override
    protected VertexData clone() throws CloneNotSupportedException {
        return (VertexData) super.clone();
    }

    @Override
    public String toString() {
        return String.format("{%s:%s:%s:%s:%s:%s:%s:%s:%s:%s}", this.verts[0], this.verts[1], this.verts[2],
                this.verts[3], this.colors[0], this.colors[1], this.colors[2], this.colors[3], this.texCoords[0],
                this.texCoords[1]);
    }

    /**
     * Converts the given data to a {@link FloatBuffer}
     * 
     * @param vds
     *            - the {@link VertexData} to use
     * @param reuse
     *            - A FloatBuffer to reuse for storing the data, or {@code null}
     *            to create a new one
     * @return a FloatBuffer with the data
     */
    public static FloatBuffer toFB(VertexData[] vds, @Nullable FloatBuffer reuse) {
        return toFB(vds, reuse, false);
    }

    /**
     * Converts the given data to a {@link FloatBuffer}
     * 
     * @param vds
     *            - the {@link VertexData} to use
     * @param reuse
     *            - A FloatBuffer to reuse for storing the data, or {@code null}
     *            to create a new one
     * @param tex
     *            - does this FB need the textures enabled?
     * @return a FloatBuffer with the data
     */
    public static FloatBuffer toFB(VertexData[] vds, @Nullable FloatBuffer reuse, boolean tex) {
        FloatBuffer ret = reuse != null ? reuse : BufferUtils.createFloatBuffer(VERTEX_SIZE * vds.length);
        DisplayLayer layer = DisplayLayer.CREATED.get(GLFW.glfwGetCurrentContext());
        Vector3f win2fb = layer.convertFromWindowToFramebuffer(Vector3f.ONE);
        for (VertexData vd : vds) {
            float[] f = vd.verts.clone();
            Vector3f v = new Vector3f(f[0], f[1], f[2]);
            Vector3f newV = PreShaderOps.apply(v).mul(win2fb);
            f[0] = newV.getX();
            f[1] = newV.getY();
            f[2] = newV.getZ();
            ret.put(f);
            ret.put(vd.colors);
            ret.put(vd.texCoords);
        }
        ret.flip();
        return ret;
    }
}
