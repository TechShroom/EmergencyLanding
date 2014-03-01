package emergencylanding.k.library.lwjgl.render;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import k.core.util.arrays.ResizableArray;
import k.core.util.strings.Strings;
import emergencylanding.k.library.lwjgl.Shapes;
import emergencylanding.k.library.lwjgl.tex.BufferedTexture;
import emergencylanding.k.library.lwjgl.tex.ELTexture;
import emergencylanding.k.library.util.DrawableUtils;
import emergencylanding.k.library.util.LUtils;

/**
 * A TrueType font implementation originally for Slick, edited for Bobjob's
 * Engine, and now for EmergencyLanding
 * 
 * @original author: James Chambers (Jimmy)
 * @original author: Jeremy Adams (elias4444)
 * @original author: Kevin Glass (kevglass)
 * @original author: Peter Korzuszek (genail)
 * 
 * @author: new version edited by David Aaron Muhar (bobjob)
 * 
 * @author Kenzie Togami (kenzierocks)
 */
public class StringRenderer {
    public final static int ALIGN_LEFT = 0, ALIGN_RIGHT = 1, ALIGN_CENTER = 2;
    /** Array that holds necessary information about the font characters */
    private ELTexture[] charArray = new ELTexture[256];

    /** Map of user defined font characters (Character <-> ELTexture) */
    private Map<Character, ELTexture> customChars = new HashMap<Character, ELTexture>();

    /** Boolean flag on whether AntiAliasing is enabled or not */
    private boolean antiAlias;

    /** Font's size */
    private int fontSize = 0;
    /** Height */
    private int fontHeight;

    /** A reference to Java's AWT Font that we create our font texture from */
    private Font font;

    private int correctL = 9, correctR = 8;

    public StringRenderer(Font font, boolean antiAlias, char[] additionalChars) {
        this(font, antiAlias, additionalChars, Color.WHITE);
    }

    public StringRenderer(Font font, boolean antiAlias) {
        this(font, antiAlias, Color.WHITE);
    }

    public StringRenderer(Font font, boolean antiAlias, char[] additionalChars,
            Color c) {
        this.font = font;
        this.fontSize = font.getSize() + 3;
        this.antiAlias = antiAlias;

        createSet(additionalChars, c);
    }

    public StringRenderer(Font font, boolean antiAlias, Color c) {
        this(font, antiAlias, null, c);
    }

    public void setCorrection(boolean on) {
        if (on) {
            correctL = 2;
            correctR = 1;
        } else {
            correctL = 0;
            correctR = 0;
        }
    }

    private void createSet(char[] customCharsArray, Color c) {
        try {

            int customCharsLength = (customCharsArray != null) ? customCharsArray.length
                    : 0;

            for (int i = 0; i < 256 + customCharsLength; i++) {

                // get 0-255 characters and then custom characters
                char ch = (i < 256) ? (char) i : customCharsArray[i - 256];

                // create image
                BufferedImage fontImage = DrawableUtils.getFontImage(ch,
                        antiAlias, font, fontSize, c);

                // inc height if needed
                fontHeight = Math.max(fontHeight, fontImage.getHeight());

                if (i < 256) {
                    // standard characters
                    charArray[i] = new BufferedTexture(fontImage);
                } else {
                    // custom characters
                    customChars.put(new Character(ch), new BufferedTexture(
                            fontImage));
                }
            }

        } catch (Exception e) {
            LUtils.print("Failed to create font.");
            e.printStackTrace();
        }
    }

    private void drawQuad(float drawX, float drawY2, float drawX2, float drawY,
            ELTexture tex) {
        // TODO: Optimize with DrawableUtils
        VBAO v = Shapes.getQuad(new VertexData().setXYZ(drawX2, drawY2, 0),
                new VertexData().setXYZ(drawX - drawX2, drawY - drawY2, 0),
                Shapes.XY);
        v.setTexture(tex);
        v.draw();
    }

    public int getWidth(String whatchars) {
        int totalwidth = 0;
        int currentChar = 0;
        for (int i = 0; i < whatchars.length(); i++) {
            currentChar = whatchars.charAt(i);
            ELTexture tex = (currentChar < 256) ? charArray[currentChar]
                    : customChars.get(new Character((char) currentChar));

            if (tex != null)
                totalwidth += tex.dim.width;
        }
        return totalwidth;
    }

    public int getHeight() {
        return fontHeight;
    }

    public int getHeight(String HeightString) {
        return fontHeight * Strings.count(HeightString, '\n');
    }

    public int getLineHeight() {
        return fontHeight;
    }

    public void drawString(float x, float y, String whatchars, float scaleX,
            float scaleY) {
        drawString(x, y, whatchars, 0, whatchars.length() - 1, scaleX, scaleY,
                ALIGN_LEFT);
    }

    public void drawString(float x, float y, String whatchars, float scaleX,
            float scaleY, int format) {
        drawString(x, y, whatchars, 0, whatchars.length() - 1, scaleX, scaleY,
                format);
    }

    public void drawString(float x, float y, String whatchars, int startIndex,
            int endIndex, float scaleX, float scaleY, int format) {

        ELTexture tex = null;
        int charCurrent;

        int totalwidth = 0;
        int i = startIndex, d, c;
        float startY = 0;

        switch (format) {
        case ALIGN_RIGHT: {
            d = -1;
            c = correctR;

            while (i < endIndex) {
                if (whatchars.charAt(i) == '\n')
                    startY -= fontHeight;
                i++;
            }
            break;
        }
        case ALIGN_CENTER: {
            for (int l = startIndex; l <= endIndex; l++) {
                charCurrent = whatchars.charAt(l);
                if (charCurrent == '\n')
                    break;
                if (charCurrent < 256) {
                    tex = charArray[charCurrent];
                } else {
                    tex = customChars.get(new Character((char) charCurrent));
                }
                totalwidth += tex.dim.width - correctL;
            }
            totalwidth /= -2;
        }
        case ALIGN_LEFT:
        default: {
            d = 1;
            c = correctL;
            break;
        }

        }

        while (i >= startIndex && i <= endIndex) {

            charCurrent = whatchars.charAt(i);
            if (charCurrent < 256) {
                tex = charArray[charCurrent];
            } else {
                tex = customChars.get(new Character((char) charCurrent));
            }

            if (tex != null) {
                if (d < 0)
                    totalwidth += (tex.dim.width - c) * d;
                if (charCurrent == '\n') {
                    startY -= fontHeight * d;
                    totalwidth = 0;
                    if (format == ALIGN_CENTER) {
                        for (int l = i + 1; l <= endIndex; l++) {
                            charCurrent = whatchars.charAt(l);
                            if (charCurrent == '\n')
                                break;
                            if (charCurrent < 256) {
                                tex = charArray[charCurrent];
                            } else {
                                tex = customChars.get(new Character(
                                        (char) charCurrent));
                            }
                            totalwidth += tex.dim.width - correctL;
                        }
                        totalwidth /= -2;
                    }
                    // if center get next lines total width/2;
                } else {
                    drawQuad((totalwidth + tex.dim.width) * scaleX + x, startY
                            * scaleY + y, totalwidth * scaleX + x,
                            (startY + tex.dim.height) * scaleY + y, tex);
                    if (d > 0)
                        totalwidth += (tex.dim.width - c) * d;
                }
                i += d;

            }
        }
    }

    public static boolean isSupported(String fontname) {
        Font font[] = getFonts();
        for (int i = font.length - 1; i >= 0; i--) {
            if (font[i].getName().equalsIgnoreCase(fontname))
                return true;
        }
        return false;
    }

    public static Font[] getFonts() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    }

    public static byte[] intToByteArray(int value) {
        return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
                (byte) (value >>> 8), (byte) value };
    }

    public void destroy() {
        System.err.println("// Cleaning Textures \\\\");
        ELTexture[] all = new ELTexture[charArray.length + customChars.size()];
        System.arraycopy(charArray, 0, all, 0, charArray.length);
        if (customChars.size() != 0) {
            ELTexture[] c = new ResizableArray<ELTexture[]>(ELTexture[].class,
                    customChars.values()).getArray();
            System.arraycopy(c, 0, all, charArray.length, c.length);
        }
        for (ELTexture t : all) {
            t.kill();
        }
        System.err.println("\\\\      Complete     //");
    }
}
