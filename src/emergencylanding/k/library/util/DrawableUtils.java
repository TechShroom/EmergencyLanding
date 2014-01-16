package emergencylanding.k.library.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import emergencylanding.k.library.lwjgl.tex.BufferedTexture;
import emergencylanding.k.library.lwjgl.tex.ELTexture;

public class DrawableUtils {

    /**
     * Perform linear interpolation on positions
     * 
     * @param pos1
     *            -The first position (does not matter x or y)
     * @param pos2
     *            -The second position
     * @param v
     *            -The interpolaty-bit. Decimal between 0 and 1.
     * @return -The position (actually an average of pos1/pos2 + pos1).
     */
    public static float lerp(float pos1, float pos2, float v) {
        return pos1 + (pos2 - pos1) * v;
    }

    public static BufferedImage scaledBufferedImage(BufferedImage image,
            int sw, int sh) {
        LUtils.print("Requested x and y of image is " + sw + " " + sh);
        LUtils.print("Actual x and y is " + image.getWidth() + " "
                + image.getHeight());
        int type = 0;
        type = image.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_ARGB
                : image.getType();
        BufferedImage resizedImage = new BufferedImage(sw, sh, type);
        Graphics2D g = resizedImage.createGraphics();
        boolean isDone = g.drawImage(image, 0, 0, sw, sh, null);
        if (!isDone) {
            LUtils.print("Scaler not done?!");
            throw new IllegalStateException(
                    "Scaling not complete, and callback not implemented");
        }
        g.dispose();
        LUtils.print("Resultant x and y of image is " + resizedImage.getWidth()
                + " " + resizedImage.getHeight());
        return resizedImage;
    }

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
    public static ByteBuffer chopBuffer(ByteBuffer buf,
            Dimension oldWidthHeight, int choppedWidth, int choppedHeight,
            int extraDimensions) {
        if (buf.capacity() < choppedWidth * choppedHeight * extraDimensions
                || oldWidthHeight.height < choppedHeight
                || oldWidthHeight.width < choppedWidth || extraDimensions < 1) {
            throw new IndexOutOfBoundsException(
                    "One of the dimensions is breaking the bounds of the buffer!");
        }
        if (buf.capacity() > buf.remaining()) {
            buf.rewind();
        }
        ByteBuffer newBuf = ByteBuffer.allocateDirect(choppedWidth
                * choppedHeight * extraDimensions);
        byte[][][] arrayOfStuff = new byte[choppedWidth][choppedHeight][extraDimensions];
        for (int i = 0; i < oldWidthHeight.width * oldWidthHeight.height
                * extraDimensions; i += extraDimensions) {
            int x = i / extraDimensions / oldWidthHeight.width;
            int y = i / extraDimensions % oldWidthHeight.height;
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

    public static ELTexture scaledTexture(ELTexture tex, int width, int height) {
        return new BufferedTexture(scaledBufferedImage(tex.toBufferedImage(),
                width, height));
    }

    public static BufferedImage getFontImage(char ch, boolean antiAlias,
            Font font, int fontSize) {
        return getFontImage(ch, antiAlias, font, fontSize, Color.WHITE);
    }

    private static final int charx = 3, chary = 1;

    public static BufferedImage getFontImage(char ch, boolean antiAlias,
            Font font, int fontSize, Color c) {
        // Create a temporary image to extract the character's size
        BufferedImage tempfontImage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
        if (antiAlias == true) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        FontMetrics fontMetrics = g.getFontMetrics();
        int charwidth = fontMetrics.charWidth(ch) + 8;

        if (charwidth <= 0) {
            charwidth = 7;
        }
        int charheight = fontMetrics.getHeight() + 3;
        if (charheight <= 0) {
            charheight = fontSize;
        }

        // Create another image holding the character we are creating
        BufferedImage fontImage = new BufferedImage(charwidth, charheight,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D gt = (Graphics2D) fontImage.getGraphics();

        if (antiAlias == true) {
            gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

        gt.setFont(font);
        gt.setColor(c);
        gt.drawString(String.valueOf(ch), (charx),
                (chary) + fontMetrics.getAscent());

        return fontImage;
    }
}
