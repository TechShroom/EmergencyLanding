package emergencylanding.k.library.lwjgl.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class VertexData {
	public static final int FLOAT_SIZE = 4;
	public static final int FLOATS_PER_POSTITON = 4;
	public static final int FLOATS_PER_COLOR = 4;
	public static final int FLOATS_PER_TEXTURE = 2;
	public static final int POSITION_SIZE = FLOATS_PER_POSTITON * FLOAT_SIZE;
	public static final int COLOR_SIZE = FLOATS_PER_COLOR * FLOAT_SIZE;
	public static final int TEXTURE_SIZE = FLOATS_PER_TEXTURE * FLOAT_SIZE;
	public static final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE
			+ TEXTURE_SIZE;

	public static final int NO_DATA = -1, COLOR_FIRST = 0, COORDS_FIRST = 1,
			TEX_FIRST = 2;

	/**
	 * Default vertex origin
	 */
	private float[] verts = {0f, 0f, 0f, 1f};

	/**
	 * Default color white
	 */
	private float[] colors = {1f, 1f, 1f, 1f};

	/**
	 * Default texture coords 0, 0
	 */
	private float[] texCoords = {0f, 0f};

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
		float x = 0, y = 0, z = 0, w = 1, r = 1, g = 1, b = 1, a = 1, u = 0, v = 0;
		switch (floats.length) {
			case 0 :
				break;
			case 3 :
				x = floats[0];
				y = floats[1];
				z = floats[2];
				break;
			case 4 :
				x = floats[0];
				y = floats[1];
				z = floats[2];
				w = floats[3];
				break;
			case 5 :
				x = floats[0];
				y = floats[1];
				z = floats[2];
				u = floats[3];
				v = floats[4];
				break;
			case 6 :
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
			case 7 :
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
			case 8 :
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
			case 9 :
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
			case 10 :
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
	 */
	public void setXYZ(float x, float y, float z) {
		setXYZW(x, y, z, 1f);
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
	 */
	public void setXYZW(float x, float y, float z, float w) {
		verts[0] = x;
		verts[1] = y;
		verts[2] = z;
		verts[3] = w;
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
	 */
	public void setRGB(float r, float g, float b) {
		setRGBA(r, g, b, 1f);
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
	 */
	public void setRGBA(float r, float g, float b, float a) {
		colors[0] = r;
		colors[1] = g;
		colors[2] = b;
		colors[3] = a;
	}

	/**
	 * Sets the u and v
	 * 
	 * @param u
	 *            - texture coord u
	 * @param v
	 *            - texture coord v
	 */
	public void setUV(float u, float v) {
		texCoords[0] = u;
		texCoords[1] = v;
	}

	/**
	 * Returns the order used to generate the vertex data
	 * 
	 * @return the order used to generate the vertex data
	 */
	public int getOrderUsed() {
		return order;
	}

	/**
	 * Converts the given data to a {@link FloatBuffer}
	 * 
	 * @param vds
	 *            - the {@link VertexData} to use
	 * @return a FloatBuffer with the data
	 */
	public static FloatBuffer toFB(VertexData[] vds) {
		FloatBuffer ret = BufferUtils.createFloatBuffer(VERTEX_SIZE
				* vds.length);
		for (VertexData vd : vds) {
			ret.put(vd.verts);
			ret.put(vd.colors);
			ret.put(vd.texCoords);
		}
		ret.flip();
		return ret;
	}
}
