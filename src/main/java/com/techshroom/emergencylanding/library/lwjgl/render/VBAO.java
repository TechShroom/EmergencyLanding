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

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;

import com.techshroom.emergencylanding.library.internalstate.Victor;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;
import com.techshroom.emergencylanding.library.lwjgl.tex.Texture;
import com.techshroom.emergencylanding.library.util.DrawableUtils;
import com.techshroom.emergencylanding.library.util.LUtils;
import com.techshroom.emergencylanding.library.util.StackTraceInfo;

/**
 * A virtual buffer/array object, used for speedy rendering.
 * 
 * 
 * @author Kenzie Togami
 * 
 */
public class VBAO implements Cloneable {

    /**
     * Index of the position attribute in the VBO
     */
    public static final int POS_VBO_INDEX = GLData.POSITION_INDEX;
    /**
     * Index of the color attribute in the VBO
     */
    public static final int COLOR_VBO_INDEX = GLData.COLOR_INDEX;
    /**
     * Index of the texCoord attribute in the VBO
     */
    public static final int TEX_VBO_INDEX = GLData.TEX_INDEX;

    /**
     * The amount of VBOs used
     */
    public static final int VBO_COUNT = 2;
    /**
     * An empty VBAO for returning valid values for invalid input.
     */
    public static final VBAO EMPTY = new VBAO(new VertexData[] { new VertexData() }, new byte[] { 0, 1, 2 }, true);

    /**
     * The vertex data
     */
    private FloatBuffer vertData = BufferUtils.createFloatBuffer(0);
    /**
     * The IC data
     */
    private ByteBuffer indexData = BufferUtils.createByteBuffer(0);
    /**
     * The amount of vertices
     */
    private int verticesCount = 0;
    /**
     * The VBO id that holds the interleaved data.
     */
    private int vbo = GLData.NONE;
    /**
     * The VBO id that holds the IC data.
     */
    private int vbo_i = GLData.NONE;
    /**
     * Determines if this data is used with {@link #GL_STATIC_DRAW} or
     * {@link #GL_DYNAMIC_DRAW}.
     */
    private boolean staticdata = false;
    /**
     * The texture used by this VBAO, if any.
     */
    public Texture tex = null;
    /**
     * The original data.
     */
    public VertexData[] data = {};
    /**
     * The original data.
     */
    public byte[] icdata = {};
    /**
     * The offset for drawing (like doing glTanslate)
     */
    public Victor xyzoffset = new Victor();

    public VBAO(VertexData[] verts, byte[] indexControl, boolean stat) {
        this(verts, indexControl, null, stat);
    }

    public VBAO(VertexData[] verts, byte[] indexControl, ELTexture t, boolean stat) {
        this.data = verts;
        this.icdata = indexControl;
        this.vertData = VertexData.toFB(verts, null);
        this.indexData = BufferUtils.createByteBuffer(indexControl.length);
        this.indexData.put(indexControl);
        this.indexData.flip();
        this.verticesCount = indexControl.length;
        this.staticdata = stat;
        this.tex = t;
        init();
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    public VBAO setTexture(ELTexture t) {
        this.tex = t;
        return this;
    }

    /**
     * 
     * @param pos
     *            - the offset
     * @return this
     * @deprecated Use
     *             {@link DrawableUtils#glBeginTrans(double, double, double)}
     *             instead.
     */
    @Deprecated
    public VBAO setXYZOff(Victor pos) {
        if (this.staticdata) {
            throw new IllegalStateException("static data");
        }
        if (pos == null) {
            throw new NullPointerException("position");
        }
        if (pos.equals(this.xyzoffset)) {
            // same position, no-op
            return this;
        }
        this.xyzoffset = pos;
        VertexData[] newDataTemp = new VertexData[this.data.length];
        for (int i = 0; i < newDataTemp.length; i++) {
            VertexData old = this.data[i];
            VertexData v = new VertexData()
                    .setXYZW(old.verts[0] + pos.x, old.verts[1] + pos.y, old.verts[2] + pos.z, old.verts[3])
                    .setRGBA(old.colors[0], old.colors[1], old.colors[2], old.colors[3])
                    .setUV(old.texCoords[0], old.texCoords[1]);
            newDataTemp[i] = v;
        }
        updateData(newDataTemp, this.icdata);
        return this;
    }

    public VBAO updateData(VertexData[] verts, byte[] indexControl) {
        if (this.staticdata) {
            throw new IllegalStateException("static data");
        }
        this.vertData = VertexData.toFB(verts, (FloatBuffer) this.vertData.rewind());
        this.indexData = BufferUtils.createByteBuffer(indexControl.length);
        this.indexData.put(indexControl);
        this.indexData.flip();
        this.verticesCount = indexControl.length;
        glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
        // null pointer the original data (disabled until LWJGL 3)
        /*
         * glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) null, GL_DYNAMIC_DRAW);
         */
        glBufferData(GL_ARRAY_BUFFER, this.vertData, GL_DYNAMIC_DRAW);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError("updateData -> overwrite vertData");
        }
        glBindVertexArray(GLData.NONE);
        // IC
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.vbo_i);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indexData, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GLData.NONE);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError("updateData -> overwrite IC");
        }
        return this;
    }

    public void setStatic(boolean stat) {
        this.staticdata = stat;
        if (!this.staticdata) {
            updateData(this.data, this.icdata);
        }
    }

    private void init() {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                // Create the vertex VBO
                createVertexVBO();

                // Create the IC VBO
                createIndexControlVBO();
                if (LUtils.debugLevel >= 1) {
                    GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
                }
            }
        };
        ELTexture.addRunnableToQueue(r);
    }

    private void createVertexVBO() {
        // Create a new Vertex Buffer Object in memory and select it (bind)
        // A VBO is a collection of Vectors used to represent data
        this.vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
        glBufferData(GL_ARRAY_BUFFER, this.vertData, (this.staticdata ? GL_STATIC_DRAW : GL_DYNAMIC_DRAW));
        // Put the VBO in the attributes list at index 0 (position)
        glVertexAttribPointer(POS_VBO_INDEX, VertexData.FLOATS_PER_POSTITON, GL_FLOAT, false, VertexData.VERTEX_SIZE,
                0);
        // Put the VBO in the attributes list at index 1 (color)
        glVertexAttribPointer(COLOR_VBO_INDEX, VertexData.FLOATS_PER_COLOR, GL_FLOAT, false, VertexData.VERTEX_SIZE,
                VertexData.POSITION_SIZE);
        // Put the VBO in the attributes list at index 2 (tex)
        glVertexAttribPointer(TEX_VBO_INDEX, VertexData.FLOATS_PER_TEXTURE, GL_FLOAT, false, VertexData.VERTEX_SIZE,
                VertexData.POSITION_SIZE + VertexData.COLOR_SIZE);
        // Deselect (bind to 0) the VBO
        glBindBuffer(GL_ARRAY_BUFFER, GLData.NONE);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    private void createIndexControlVBO() {
        this.vbo_i = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.vbo_i);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indexData, (this.staticdata ? GL_STATIC_DRAW : GL_DYNAMIC_DRAW));
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GLData.NONE);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    public VBAO draw() {
        if (this.vbo == GLData.NONE || this.vbo_i == GLData.NONE) {
            // no longer valid!
            destroy();
            return null;
        }

        // apply preshaderops if not static
        if (!this.staticdata)
            updateData(this.data, this.icdata);

        if (this.tex != null) {
            this.tex.bind();
        }

        glUniform1f(GLData.uniformTexEnabler, (this.tex == null ? 0 : 1));

        glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
        glEnableVertexAttribArray(POS_VBO_INDEX);
        glEnableVertexAttribArray((this.tex == null) ? COLOR_VBO_INDEX : TEX_VBO_INDEX);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.vbo_i);

        vertexDraw();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GLData.NONE);

        // Put everything back to default (deselect)
        glDisableVertexAttribArray(POS_VBO_INDEX);
        glDisableVertexAttribArray((this.tex == null) ? COLOR_VBO_INDEX : TEX_VBO_INDEX);
        glBindBuffer(GL_ARRAY_BUFFER, GLData.NONE);

        if (this.tex != null) {
            this.tex.unbind();
        }
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
        return this;
    }

    private void vertexDraw() {
        // Draw the vertices
        glDrawElements(GL_TRIANGLES, this.verticesCount, GL_UNSIGNED_BYTE, 0);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    public void destroy() {
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError("destroy-itsnotmyfault");
        }
        // Disable the VBO index from the VAO attributes list
        // glDisableVertexAttribArray(POS_VBO_INDEX);
        // glDisableVertexAttribArray(COLOR_VBO_INDEX);
        // glDisableVertexAttribArray(TEX_VBO_INDEX);

        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError("destroy-DVAA");
        }

        deleteVertexVBO();

        // Delete the VAO
        glBindVertexArray(GLData.NONE);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    private void deleteVertexVBO() {
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError("deleteVertexVBO-itwasntme");
        }
        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, GLData.NONE);
        glDeleteBuffers(this.vbo);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(this.data) + "->" + Arrays.toString(this.icdata) + " ("
                + (this.staticdata ? "static" : "dynamic") + ")";
    }

    @Override
    public VBAO clone() {
        VBAO c = null;
        try {
            c = (VBAO) super.clone();
            c.data = this.data.clone();
            c.icdata = this.icdata.clone();
            c.indexData = this.indexData.duplicate();
            c.tex = this.tex;
            c.vbo = this.vbo;
            c.vbo_i = this.vbo_i;
            c.vertData = this.vertData.duplicate();
            c.verticesCount = this.verticesCount;
            c.xyzoffset = this.xyzoffset;
            c.staticdata = this.staticdata;
        } catch (CloneNotSupportedException e) {
            throw new InternalError("not clonable");
        }
        return c;
    }

}