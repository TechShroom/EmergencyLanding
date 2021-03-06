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
package com.techshroom.emergencylanding.library.lwjgl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;

import com.techshroom.emergencylanding.library.lwjgl.render.VBAO;
import com.techshroom.emergencylanding.library.lwjgl.render.VertexData;
import com.techshroom.emergencylanding.library.util.LUtils;

public class Shapes {

    /**
     * Tells {@link Shapes#getQuad(VertexData, VertexData, int)} how to use the
     * two data points
     */
    public static final int XY = 0x01;
    /**
     * Tells {@link Shapes#getQuad(VertexData, VertexData, int)} how to use the
     * two data points
     */
    public static final int XZ = 0x02;
    /**
     * Tells {@link Shapes#getQuad(VertexData, VertexData, int)} how to use the
     * two data points
     */
    public static final int YZ = 0x03;

    private static final byte[] TRI_IC = { 0, 1, 2 };
    private static final byte[] QUAD_IC = { 0, 1, 2, 2, 3, 0 };
    private static final byte[] CUBE_IC = { 0, 1, 2, 3, 0, 4, 5, 0, 6, 3, 6, 0, 0, 2, 4, 5, 1, 0, 2, 1, 5, 7, 6, 3, 6,
            7, 5, 7, 3, 4, 7, 4, 2, 7, 2, 5 };
    private static final byte[] SPHERE_IC = { 0, 1, 2, 2, 3, 0 };

    private static HashMap<String, Integer> shapes = new HashMap<String, Integer>();
    {
        Shapes.shapes.put("cube", 0);
        Shapes.shapes.put("quad", 1);
        Shapes.shapes.put("sphere", 2);
    }

    public static ArrayList<String> getSupportedShapes() {
        return new ArrayList<String>(Shapes.shapes.keySet());
    }

    /**
     * Triangles do not care about vertex order.
     * 
     * @param vertices
     *            - the vertices to use
     * @return a {@link VBAO} for use in drawing.
     */
    public static VBAO getTriangle(VertexData[] vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("vertices cannot be null");
        }
        if (vertices.length != 3) {
            LUtils.print("Triangle requires ONLY 3 vertices");
            return VBAO.EMPTY;
        }
        return new VBAO(vertices, TRI_IC, true);
    }

    /**
     * Uses two sets of data to make the vertices for
     * {@link Shapes#getQuad(VertexData[])}
     * 
     * @param data
     *            - the first vertex. Uses the color from this one, and treats
     *            its xyz as the xyz coords
     * @param data2
     *            - the second vertex. Uses its xyz as the width, length, and
     *            height
     * @param dir
     *            - one of {@link Shapes#XY}, {@link Shapes#XZ}, or
     *            {@link Shapes#YZ}
     * @return - a {@link VBAO} for drawing
     */
    public static VBAO getQuad(VertexData data, VertexData data2, int dir) {
        VertexData[] data_array = new VertexData[4];
        float[] color = data.colors;
        for (int i = 0; i < data_array.length; i++) {
            data_array[i] = new VertexData();
            data_array[i].setRGBA(color[0], color[1], color[2], color[3]);
        }
        float[] vert = data.verts;
        float x = vert[0];
        float y = vert[1];
        float z = vert[2];
        float[] vert2 = data2.verts;
        float w = vert2[0] + x;
        float h = vert2[1] + y;
        float l = vert2[2] + z;
        if (dir == Shapes.XZ) {
            data_array[0].setXYZ(x, y, l);// Top (x:0,y=y,z:0)
            data_array[0].setUV(0, 0);
            data_array[1].setXYZ(x, y, z);
            data_array[1].setUV(0, 1);
            data_array[2].setXYZ(w, y, z);
            data_array[2].setUV(1, 1);
            data_array[3].setXYZ(z, y, l);
            data_array[3].setUV(1, 0);
        }
        if (dir == Shapes.XY) {
            data_array[0].setXYZ(x, h, z);// Front (x:0,y:0,z=z)
            data_array[0].setUV(0, 0);
            data_array[1].setXYZ(x, y, z);
            data_array[1].setUV(0, 1);
            data_array[2].setXYZ(w, y, z);
            data_array[2].setUV(1, 1);
            data_array[3].setXYZ(w, h, z);
            data_array[3].setUV(1, 0);
        }
        if (dir == Shapes.YZ) {
            data_array[0].setXYZ(x, y, l);// Left (x=x,y:0,z:0)
            data_array[0].setUV(0, 0);
            data_array[1].setXYZ(x, y, z);
            data_array[1].setUV(0, 1);
            data_array[2].setXYZ(x, h, z);
            data_array[2].setUV(1, 1);
            data_array[3].setXYZ(x, h, l);
            data_array[3].setUV(1, 0);
        }
        return getQuad(data_array);
    }

    /**
     * Quads must have vertex data like this: <br>
     * <code>
     * 1------4<br>
     * |......|<br>
     * |......|<br>
     * |......|<br>
     * 2------3<br>
     * </code>
     * 
     * @param vertices
     *            - the vertices to use
     * @return a {@link VBAO} for use in drawing.
     */
    public static VBAO getQuad(VertexData[] vertices) {
        if (vertices == null || Stream.of(vertices).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("vertices cannot be null");
        }
        if (vertices.length != 4) {
            LUtils.print("Quad requires ONLY 4 vertices");
            return VBAO.EMPTY;
        }
        return new VBAO(vertices, QUAD_IC, true);
    }

    /**
     * Cubes must have vertex data like this: <br>
     * Front:<br>
     * <code>
     * 1------4<br>
     * |......|<br>
     * |......|<br>
     * |......|<br>
     * 2------3<br>
     * </code> <br>
     * Back:<br>
     * <code>
     * 5------8<br>
     * |......|<br>
     * |......|<br>
     * |......|<br>
     * 6------7<br>
     * </code>
     * 
     * @param vertices
     *            - the vertices to use
     * @return a {@link VBAO} for use in drawing.
     */
    public static VBAO getCube(VertexData[] vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("vertices cannot be null");
        }
        if (vertices.length != 8) {
            LUtils.print("Cube requires ONLY 8 vertices");
            return VBAO.EMPTY;
        }
        return new VBAO(vertices, CUBE_IC, true);
    }

    /**
     * Deprecated due to non-working state
     * 
     * @param center
     *            - Center point vertex data
     * @param radius
     *            - Radius of the sphere
     * @return a VBAO representing the sphere
     */
    @Deprecated
    public static VBAO getSphere(VertexData center, float radius) {
        if (center == null) {
            throw new IllegalArgumentException("center cannot be null");
        }
        VertexData[] vertices = null;
        return getSphere(vertices);
    }

    /**
     * Deprecated due to non-working state
     * 
     * @param vertices
     *            - Vertex data for the sphere
     * @return a VBAO representing the sphere
     */
    @Deprecated
    public static VBAO getSphere(VertexData[] vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("vertices cannot be null");
        }
        return new VBAO(vertices, SPHERE_IC, true);
    }
}
