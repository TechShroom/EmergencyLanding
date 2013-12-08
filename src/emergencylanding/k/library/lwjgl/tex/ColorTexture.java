package emergencylanding.k.library.lwjgl.tex;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import k.core.reflect.Reflect;

import org.lwjgl.util.Color;

public class ColorTexture extends Texture {
	public static final ColorTexture RED = new ColorTexture(java.awt.Color.RED);
	public static final ColorTexture BLUE = new ColorTexture(
			java.awt.Color.BLUE);
	public static final ColorTexture GREEN = new ColorTexture(
			java.awt.Color.GREEN);
	public static final ColorTexture YELLOW = new ColorTexture(
			java.awt.Color.YELLOW);
	public static final ColorTexture WHITE = new ColorTexture(
			java.awt.Color.WHITE);
	public static final ColorTexture PURPLE = new ColorTexture(
			java.awt.Color.MAGENTA);
	private Color c = null;

	public ColorTexture(java.awt.Color from, Dimension size) {
		c = new Color(from.getRed(), from.getGreen(), from.getBlue(),
				from.getAlpha());
		// System.out.println(String.format("color is %s %s %s %s",
		// from.getRed(),
		// from.getGreen(), from.getBlue(), from.getAlpha()));
		// System.out.println(String.format("Lcolor is %s %s %s %s", c.getRed(),
		// c.getGreen(), c.getBlue(), c.getAlpha()));
		dim = new Dimension(size);
		super.init();
	}

	public ColorTexture(java.awt.Color from) {
		this(from, new Dimension(2, 2));
	}

	@Override
	public void setup() {
		System.err.println("The RGBA values from c are these "
				+ String.format("%s %s %s %s", c.getRedByte() & 0xFF,
						c.getGreenByte() & 0xFF, c.getBlueByte() & 0xFF,
						c.getAlphaByte() & 0xFF));
		buf = ByteBuffer.allocateDirect(4 * dim.width * dim.height);
		for (int i = 0; i < buf.capacity(); i += 4) {
			c.writeRGB(buf);
			buf.put((byte) (c.getAlphaByte() & 0xFF));
		}
	}

	@Override
	public boolean isLookAlike(Texture t) {
		if (t instanceof ColorTexture) {
			return c.equals(((ColorTexture) t).c) && dim.equals(t.dim);
		} else {
			return super.isLookAlike(t);
		}
	}

	public java.awt.Color getRawColor() {
		return new java.awt.Color(c.getRed(), c.getGreen(), c.getBlue(),
				c.getAlpha());
	}

	@Override
	protected BufferedImage toBufferedImageAbstract() {
		BufferedImage ret = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = ret.getGraphics();
		g.setColor(getRawColor());
		g.fillRect(0, 0, getWidth(), getHeight());
		return ret;
	}

	public static ColorTexture getColor(String color) {
		String allCaps = color.toUpperCase();
		try {
			return new ColorTexture(Reflect.getFieldStatic(
					java.awt.Color.class, java.awt.Color.class, allCaps));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return ColorTexture.WHITE;
	}

	@Override
	protected void onDestruction() {
	}
}
