package emergencylanding.k.library.lwjgl;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import k.core.util.Helper;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.Sphere;

import emergencylanding.k.library.lwjgl.tex.ColorTexture;
import emergencylanding.k.library.lwjgl.tex.Texture;
import emergencylanding.k.library.util.LUtils;

public class Shapes {

	public static final int XYF = 0x01;
	public static final int XZT = 0x02;
	public static final int YZL = 0x03;
	public static final int XYB = -0x01;
	public static final int XZB = -0x02;
	public static final int YZR = -0x03;

	private static HashMap<String, Integer> shapes = new HashMap<String, Integer>();
	{
		Shapes.shapes.put("cube", 0);
		Shapes.shapes.put("quad", 1);
		Shapes.shapes.put("sphere", 2);
	}

	public static ArrayList<String> getSupportedShapes() {
		return new ArrayList<String>(Shapes.shapes.keySet());
	}

	public static void glShapeByName(float x, float y, float z,
			float[] verticies, Object[] extra, String name) {
		int shape = Shapes.shapes.get(name) == null ? 0 : Shapes.shapes
				.get(name);
		switch (shape) {
			case 0 :
				Shapes.glCube(x, y, z, verticies[0], verticies[1],
						verticies[2],
						Helper.Arrays.arrayTranslate(Texture.class, extra));
				break;
			case 1 :
				Shapes.glQuad(x, y, z, verticies[0], verticies[1],
						verticies[2], (Integer) extra[0], (Texture) extra[1]);
				break;
			case 2 :
				Shapes.glSphere(x, y, z, (Float) extra[0], (Integer) extra[1],
						(Texture) extra[2]);
				break;
		}
	}

	public static void glCube(float x, float y, float z, float w, float h,
			float l, Texture[] tex) {
		Shapes.glQuad(x, y, z, w, h, l, Shapes.XZT,
				LUtils.getArg(tex, 2, ColorTexture.GREEN));
		Shapes.glQuad(x, y, z, w, h, l, Shapes.XZB,
				LUtils.getArg(tex, 3, ColorTexture.YELLOW));
		Shapes.glQuad(x, y, z, w, h, l, Shapes.XYF,
				LUtils.getArg(tex, 0, ColorTexture.RED));
		Shapes.glQuad(x, y, z, w, h, l, Shapes.XYB,
				LUtils.getArg(tex, 1, ColorTexture.BLUE));
		Shapes.glQuad(x, y, z, w, h, l, Shapes.YZL,
				LUtils.getArg(tex, 4, ColorTexture.WHITE));
		Shapes.glQuad(x, y, z, w, h, l, Shapes.YZR,
				LUtils.getArg(tex, 5, ColorTexture.PURPLE));
	}

	public static void glQuad(float x, float y, float z, float w, float h,
			float l, int dir, Texture t) {
		if (t == null) {
			t = ColorTexture.WHITE;
		}
		t.bind();
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glBegin(GL11.GL_QUADS);
		if (dir == Shapes.XZT) {
			GL11.glVertex3f(w, h, 0);// Top (x:0,y=h,z:0)
			t.glTextureVertex(0, 0);
			GL11.glVertex3f(0, h, 0);
			t.glTextureVertex(0, 1);
			GL11.glVertex3f(0, h, l);
			t.glTextureVertex(1, 1);
			GL11.glVertex3f(w, h, l);
			t.glTextureVertex(1, 0);
		}
		if (dir == Shapes.XZB) {
			GL11.glVertex3f(w, 0, l);// Bottom (x:0,y=0,z:0)
			t.glTextureVertex(0, 0);
			GL11.glVertex3f(0, 0, l);
			t.glTextureVertex(0, 1);
			GL11.glVertex3f(0, 0, 0);
			t.glTextureVertex(1, 1);
			GL11.glVertex3f(w, 0, 0);
			t.glTextureVertex(1, 0);
		}
		if (dir == Shapes.XYF) {
			GL11.glVertex3f(w, h, l);// Front (x:0,y:0,z=l)
			t.glTextureVertex(0, 0);
			GL11.glVertex3f(0, h, l);
			t.glTextureVertex(0, 1);
			GL11.glVertex3f(0, 0, l);
			t.glTextureVertex(1, 1);
			GL11.glVertex3f(w, 0, l);
			t.glTextureVertex(1, 0);
		}
		if (dir == Shapes.XYB) {
			GL11.glVertex3f(w, 0, 0);// Back (x:0,y:0,z=0)
			t.glTextureVertex(0, 0);
			GL11.glVertex3f(0, 0, 0);
			t.glTextureVertex(0, 1);
			GL11.glVertex3f(0, h, 0);
			t.glTextureVertex(1, 1);
			GL11.glVertex3f(w, h, 0);
			t.glTextureVertex(1, 0);
		}
		if (dir == Shapes.YZL) {
			GL11.glVertex3f(0, h, l);// Left (x=0,y:0,z:0)
			t.glTextureVertex(0, 0);
			GL11.glVertex3f(0, h, 0);
			t.glTextureVertex(0, 1);
			GL11.glVertex3f(0, 0, 0);
			t.glTextureVertex(1, 1);
			GL11.glVertex3f(0, 0, l);
			t.glTextureVertex(1, 0);
		}
		if (dir == Shapes.YZR) {
			GL11.glVertex3f(w, h, 0);// Right (x=w,y:0,z:0)
			t.glTextureVertex(0, 0);
			GL11.glVertex3f(w, h, l);
			t.glTextureVertex(0, 1);
			GL11.glVertex3f(w, 0, l);
			t.glTextureVertex(1, 1);
			GL11.glVertex3f(w, 0, 0);
			t.glTextureVertex(1, 0);
		}
		GL11.glEnd();

		GL11.glPopMatrix();
	}

	public static void glSphere(float x, float y, float z, float r,
			int quality, Texture t) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		Sphere s = new Sphere();
		s.setTextureFlag(true);
		t.bind();
		s.draw(r, quality, quality);
		GL11.glPopMatrix();
	}

	public static void renderVBO(int vao, int vCount) {

		// Draw

		// Bind to the VAO that has all the information about the quad vertices
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);

		// Draw the vertices
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vCount);

		// Put everything back to default (deselect)
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
