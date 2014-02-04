package emergencylanding.k.library.lwjgl.render;

import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.opengl.GL15.*;

import static org.lwjgl.opengl.GL20.*;

import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import k.core.util.classes.StackTraceInfo;

import org.lwjgl.BufferUtils;

import emergencylanding.k.library.internalstate.Victor;
import emergencylanding.k.library.lwjgl.tex.ELTexture;
import emergencylanding.k.library.util.LUtils;

/**
 * A virtual buffer/array object, used for speedy rendering.
 * 
 * 
 * @author Kenzie Togami
 * 
 */
public class VBAO implements Cloneable {
    /**
     * Index of the position attribute in the {@link VBAO#vbo vbo}
     */
    public static final int POS_VBO_INDEX = GLData.POSITION_INDEX;
    /**
     * Index of the color attribute in the {@link VBAO#vbo vbo}
     */
    public static final int COLOR_VBO_INDEX = GLData.COLOR_INDEX;
    /**
     * Index of the texCoord attribute in the {@link VBAO#vbo vbo}
     */
    public static final int TEX_VBO_INDEX = GLData.TEX_INDEX;

    /**
     * The amount of VBOs used
     */
    public static final int VBO_COUNT = 2;
    /**
     * An empty VBAO for returning valid values for invalid input.
     */
    public static final VBAO EMPTY = new VBAO(
            new VertexData[] { new VertexData() }, new byte[] { 0, 1, 2 }, true);

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
     * The ID of the VAO array
     */
    private int vaoId = GLData.NONE;
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
     * Determines if the vertices should be a separate VBO from the color/tex
     * data.
     */
    private boolean sepVert = false;
    /**
     * The texture used by this VBAO, if any.
     */
    public ELTexture tex = null;
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

    public VBAO(VertexData[] verts, byte[] indexControl, boolean stat,
            boolean seperateVerts) {
        this(verts, indexControl, null, stat, seperateVerts);
    }

    public VBAO(VertexData[] verts, byte[] indexControl, ELTexture t,
            boolean stat) {
        this(verts, indexControl, t, stat, false);
    }

    public VBAO(VertexData[] verts, byte[] indexControl, ELTexture t,
            boolean stat, boolean seperateVerts) {
        data = verts;
        icdata = indexControl;
        vertData = VertexData.toFB(verts);
        indexData = BufferUtils.createByteBuffer(indexControl.length);
        indexData.put(indexControl);
        indexData.flip();
        verticesCount = indexControl.length;
        staticdata = stat;
        sepVert = seperateVerts;
        tex = t;
        init();
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    public VBAO setTexture(ELTexture t) {
        tex = t;
        return this;
    }

    public VBAO setXYZOff(Victor pos) {
        if (staticdata) {
            throw new IllegalStateException("static data");
        }
        if (pos == null) {
            throw new NullPointerException("position");
        }
        if (pos.equals(xyzoffset)) {
            // same position, no-op
            return this;
        }
        xyzoffset = pos;
        VertexData[] newDataTemp = new VertexData[data.length];
        for (int i = 0; i < newDataTemp.length; i++) {
            VertexData old = data[i];
            VertexData v = new VertexData()
                    .setXYZW(old.verts[0] + pos.x, old.verts[1] + pos.y,
                            old.verts[2] + pos.z, old.verts[3])
                    .setRGBA(old.colors[0], old.colors[1], old.colors[2],
                            old.colors[3])
                    .setUV(old.texCoords[0], old.texCoords[1]);
            newDataTemp[i] = v;
        }
        updateData(newDataTemp, icdata);
        return this;
    }

    public VBAO updateData(VertexData[] verts, byte[] indexControl) {
        if (staticdata) {
            throw new IllegalStateException("static data");
        }
        vertData = VertexData.toFB(verts);
        indexData = BufferUtils.createByteBuffer(indexControl.length);
        indexData.put(indexControl);
        indexData.flip();
        verticesCount = indexControl.length;
        // Overwrite data
        glBindVertexArray(vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        // null pointer the original data (disabled until LWJGL 3)
        /*
         * glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) null, GL_DYNAMIC_DRAW);
         */
        glBufferData(GL_ARRAY_BUFFER, vertData, GL_DYNAMIC_DRAW);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError("updateData -> overwrite vertData");
        }
        glBindVertexArray(GLData.NONE);
        // IC
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo_i);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GLData.NONE);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError("updateData -> overwrite IC");
        }
        return this;
    }

    private void init() {
        // Create a new Vertex Array Object in memory and select it (bind)
        // A VAO can have up to 16 attributes (VBO's) assigned to it by default
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create the vertex VBO
        createVertexVBO();

        // Deselect (bind to 0) the VAO
        glBindVertexArray(GLData.NONE);

        // Create the IC VBO
        createIndexControlVBO();
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    private void createVertexVBO() {
        // Create a new Vertex Buffer Object in memory and select it (bind)
        // A VBO is a collection of Vectors used to represent data
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertData, (staticdata ? GL_STATIC_DRAW
                : GL_DYNAMIC_DRAW));
        // Put the VBO in the attributes list at index 0 (position)
        glVertexAttribPointer(POS_VBO_INDEX, VertexData.FLOATS_PER_POSTITON,
                GL_FLOAT, false, VertexData.VERTEX_SIZE, 0);
        // Put the VBO in the attributes list at index 1 (color)
        glVertexAttribPointer(COLOR_VBO_INDEX, VertexData.FLOATS_PER_COLOR,
                GL_FLOAT, false, VertexData.VERTEX_SIZE,
                VertexData.POSITION_SIZE);
        // Put the VBO in the attributes list at index 2 (tex)
        glVertexAttribPointer(TEX_VBO_INDEX, VertexData.FLOATS_PER_TEXTURE,
                GL_FLOAT, false, VertexData.VERTEX_SIZE,
                VertexData.POSITION_SIZE + VertexData.COLOR_SIZE);
        // Deselect (bind to 0) the VBO
        glBindBuffer(GL_ARRAY_BUFFER, GLData.NONE);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    private void createIndexControlVBO() {
        vbo_i = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo_i);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData,
                (staticdata ? GL_STATIC_DRAW : GL_DYNAMIC_DRAW));
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GLData.NONE);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    public VBAO draw() {

        if (tex != null) {
            tex.bind();
        }

        glUniform1f(GLData.uniformTexEnabler, (tex == null ? 0 : 1));

        // Bind to the VAO that has all the information about the vertices
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(POS_VBO_INDEX);
        glEnableVertexAttribArray((tex == null) ? COLOR_VBO_INDEX
                : TEX_VBO_INDEX);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo_i);

        vertexDraw();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GLData.NONE);

        // Put everything back to default (deselect)
        glDisableVertexAttribArray(POS_VBO_INDEX);
        glDisableVertexAttribArray((tex == null) ? COLOR_VBO_INDEX
                : TEX_VBO_INDEX);
        glBindVertexArray(GLData.NONE);

        if (tex != null) {
            tex.unbind();
        }
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
        return this;
    }

    private void vertexDraw() {
        // Draw the vertices
        glDrawElements(GL_TRIANGLES, verticesCount, GL_UNSIGNED_BYTE, 0);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    public void destroy() {
        // Disable the VBO index from the VAO attributes list
        glDisableVertexAttribArray(POS_VBO_INDEX);
        glDisableVertexAttribArray(COLOR_VBO_INDEX);
        glDisableVertexAttribArray(TEX_VBO_INDEX);

        deleteVertexVBO();

        // Delete the VAO
        glBindVertexArray(GLData.NONE);
        glDeleteVertexArrays(vaoId);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    private void deleteVertexVBO() {
        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, GLData.NONE);
        glDeleteBuffers(vbo);
        if (LUtils.debugLevel >= 1) {
            GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(data) + "->" + Arrays.toString(icdata) + " ("
                + (staticdata ? "static" : "dynamic") + ")";
    }

    @Override
    public VBAO clone() {
        VBAO c = null;
        try {
            c = (VBAO) super.clone();
            c.data = data.clone();
            c.icdata = icdata.clone();
            c.indexData = indexData.duplicate();
            c.tex = tex;
            c.vaoId = vaoId;
            c.vbo = vbo;
            c.vbo_i = vbo_i;
            c.vertData = vertData.duplicate();
            c.verticesCount = verticesCount;
            c.xyzoffset = xyzoffset;
            c.staticdata = staticdata;
        } catch (CloneNotSupportedException e) {
            throw new InternalError("not clonable");
        }
        return c;
    }

}