package emergencylanding.k.library.lwjgl.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import emergencylanding.k.library.internalstate.Victor;
import emergencylanding.k.library.lwjgl.tex.Texture;
import emergencylanding.k.library.util.StackTraceInfo;

/**
 * A virtual buffer/array object, used for speedy rendering.
 * 
 * 
 * @author Kenzie Togami
 * 
 */
public class VBAO {
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
	    new VertexData[] { new VertexData() }, new byte[] { 0, 1, 2 });

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

    public VBAO(VertexData[] verts, byte[] indexControl) {
	this(verts, indexControl, null);
    }

    public VBAO(VertexData[] verts, byte[] indexControl, Texture t) {
	data = verts;
	icdata = indexControl;
	vertData = VertexData.toFB(verts);
	indexData = BufferUtils.createByteBuffer(indexControl.length);
	indexData.put(indexControl);
	indexData.flip();
	verticesCount = indexControl.length;
	tex = t;
	init();
	GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    public void setTexture(Texture t) {
	tex = t;
    }

    public void setXYZOff(Victor pos) {
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
    }

    public void updateData(VertexData[] verts, byte[] indexControl) {
	vertData = VertexData.toFB(verts);
	indexData = BufferUtils.createByteBuffer(indexControl.length);
	indexData.put(indexControl);
	indexData.flip();
	verticesCount = indexControl.length;
	// Overwrite data
	GL30.glBindVertexArray(vaoId);
	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertData, GL15.GL_STATIC_DRAW);
	GLData.notifyOnGLError("updateData -> overwrite vertData");
	GL30.glBindVertexArray(GLData.NONE);
	// IC
	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo_i);
	GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData,
		GL15.GL_STATIC_DRAW);
	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GLData.NONE);
	GLData.notifyOnGLError("updateData -> overwrite IC");
	//
    }

    private void init() {
	// Create a new Vertex Array Object in memory and select it (bind)
	// A VAO can have up to 16 attributes (VBO's) assigned to it by default
	vaoId = GL30.glGenVertexArrays();
	GL30.glBindVertexArray(vaoId);

	// Create the vertex VBO
	createVertexVBO();

	// Deselect (bind to 0) the VAO
	GL30.glBindVertexArray(GLData.NONE);

	// Create the IC VBO
	createIndexControlVBO();
	GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private void createVertexVBO() {
	// Create a new Vertex Buffer Object in memory and select it (bind)
	// A VBO is a collection of Vectors used to represent data
	vbo = GL15.glGenBuffers();
	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertData, GL15.GL_STATIC_DRAW);
	// Put the VBO in the attributes list at index 0 (position)
	GL20.glVertexAttribPointer(POS_VBO_INDEX,
		VertexData.FLOATS_PER_POSTITON, GL11.GL_FLOAT, false,
		VertexData.VERTEX_SIZE, 0);
	// Put the VBO in the attributes list at index 1 (color)
	GL20.glVertexAttribPointer(COLOR_VBO_INDEX,
		VertexData.FLOATS_PER_COLOR, GL11.GL_FLOAT, false,
		VertexData.VERTEX_SIZE, VertexData.POSITION_SIZE);
	// Put the VBO in the attributes list at index 2 (tex)
	GL20.glVertexAttribPointer(TEX_VBO_INDEX,
		VertexData.FLOATS_PER_TEXTURE, GL11.GL_FLOAT, false,
		VertexData.VERTEX_SIZE, VertexData.POSITION_SIZE
			+ VertexData.COLOR_SIZE);
	// Deselect (bind to 0) the VBO
	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GLData.NONE);
	GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private void createIndexControlVBO() {
	vbo_i = GL15.glGenBuffers();
	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo_i);
	GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData,
		GL15.GL_STATIC_DRAW);
	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GLData.NONE);
	GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    public void draw() {

	if (tex != null) {
	    tex.bind();
	}

	GL20.glUniform1f(GLData.uniformTexEnabler, (tex == null ? 0 : 1));

	// Bind to the VAO that has all the information about the vertices
	GL30.glBindVertexArray(vaoId);
	GL20.glEnableVertexAttribArray(POS_VBO_INDEX);
	GL20.glEnableVertexAttribArray((tex == null) ? COLOR_VBO_INDEX
		: TEX_VBO_INDEX);

	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo_i);

	vertexDraw();

	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GLData.NONE);

	// Put everything back to default (deselect)
	GL20.glDisableVertexAttribArray(POS_VBO_INDEX);
	GL20.glDisableVertexAttribArray((tex == null) ? COLOR_VBO_INDEX
		: TEX_VBO_INDEX);
	GL30.glBindVertexArray(GLData.NONE);

	if (tex != null) {
	    tex.unbind();
	}
	GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private void vertexDraw() {
	// Draw the vertices
	GL11.glDrawElements(GL11.GL_TRIANGLES, verticesCount,
		GL11.GL_UNSIGNED_BYTE, 0);
	GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    public void destroy() {
	// Disable the VBO index from the VAO attributes list
	GL20.glDisableVertexAttribArray(POS_VBO_INDEX);
	GL20.glDisableVertexAttribArray(COLOR_VBO_INDEX);
	GL20.glDisableVertexAttribArray(TEX_VBO_INDEX);

	deleteVertexVBO();

	// Delete the VAO
	GL30.glBindVertexArray(GLData.NONE);
	GL30.glDeleteVertexArrays(vaoId);
	GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }

    private void deleteVertexVBO() {
	// Delete the VBO
	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GLData.NONE);
	GL15.glDeleteBuffers(vbo);
	GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
    }
}