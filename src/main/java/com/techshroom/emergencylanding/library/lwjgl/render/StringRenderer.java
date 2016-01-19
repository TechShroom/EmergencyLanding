/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <http://techshoom.com>
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
//package com.techshroom.emergencylanding.library.lwjgl.render;
//
//import static com.techshroom.emergencylanding.library.util.DrawableUtils.glBeginScale;
//import static com.techshroom.emergencylanding.library.util.DrawableUtils.glBeginTrans;
//import static com.techshroom.emergencylanding.library.util.DrawableUtils.glEndScale;
//import static com.techshroom.emergencylanding.library.util.DrawableUtils.glEndTrans;
//
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.GraphicsEnvironment;
//import java.awt.image.BufferedImage;
//import java.nio.ByteBuffer;
//import java.nio.file.Path;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.lwjgl.stb.STBTruetype;
//
//import com.techshroom.emergencylanding.library.lwjgl.Shapes;
//import com.techshroom.emergencylanding.library.lwjgl.tex.BufferedTexture;
//import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;
//import com.techshroom.emergencylanding.library.util.DrawableUtils;
//import com.techshroom.emergencylanding.library.util.LUtils;
//import com.techshroom.emergencylanding.library.util.Strings;
//
///**
// * A TrueType font implementation originally for Slick, edited for Bobjob's
// * Engine, and now for EmergencyLanding.
// * 
// * @original author: James Chambers (Jimmy)
// * @original author: Jeremy Adams (elias4444)
// * @original author: Kevin Glass (kevglass)
// * @original author: Peter Korzuszek (genail)
// * 
// * @author: new version edited by David Aaron Muhar (bobjob)
// * 
// * @author Kenzie Togami (kenzierocks)
// */
//public class StringRenderer {
//
//    public final static int ALIGN_LEFT = 0, ALIGN_RIGHT = 1, ALIGN_CENTER = 2;
//
//    private static class VBAOChar {
//
//        private ELTexture tex = null;
//        private VBAO quad = null;
//
//        private VBAOChar(ByteBuffer charImage, int width, int height) {
//            this.tex = new BufferedTexture(charImage, width, height);
//            this.quad = Shapes.getQuad(new VertexData(), new VertexData()
//                    .setXYZ(this.tex.getWidth(), this.tex.getHeight(), 0),
//                    Shapes.XY);
//            this.quad.setTexture(this.tex);
//            this.quad.setStatic(false);
//        }
//
//        private void destroy() {
//            this.tex.kill();
//            this.quad.destroy();
//        }
//    }
//
//    /** Array that holds necessary information about the font characters */
//    private VBAOChar[] charArray = new VBAOChar[256];
//
//    /** Map of user defined font characters (Character <-> VBAOChar) */
//    private Map<Character, VBAOChar> customChars =
//            new HashMap<Character, VBAOChar>();
//
//    /** Boolean flag on whether AntiAliasing is enabled or not */
//    private boolean antiAlias;
//
//    /** Font's size */
//    private int fontSize = 0;
//    /** Height */
//    private int fontHeight;
//
//    private Path font;
//
//    private int correctL = 9, correctR = 8;
//
//    public StringRenderer(Path font, int fontSize, boolean antiAlias,
//            char[] additionalChars) {
//        this(font, fontSize, antiAlias, additionalChars, Color.WHITE);
//    }
//
//    public StringRenderer(Path font, int fontSize, boolean antiAlias) {
//        this(font, fontSize, antiAlias, Color.WHITE);
//    }
//
//    public StringRenderer(Path font, int fontSize, boolean antiAlias,
//            char[] additionalChars, Color c) {
//        this.font = font;
//        this.fontSize = fontSize;
//        this.antiAlias = antiAlias;
//
//    }
//
//    public StringRenderer(Path font, int fontSize, boolean antiAlias, Color c) {
//        this(font, fontSize, antiAlias, null, c);
//    }
//
//    public void setCorrection(boolean on) {
//        if (on) {
//            this.correctL = 2;
//            this.correctR = 1;
//        } else {
//            this.correctL = 0;
//            this.correctR = 0;
//        }
//    }
//
//    private void createSet(char[] customCharsArray, Color c) {
//        try {
//
//            int customCharsLength =
//                    (customCharsArray != null) ? customCharsArray.length : 0;
//
//            for (int i = 0; i < 256 + customCharsLength; i++) {
//
//                // get 0-255 characters and then custom characters
//                char ch = (i < 256) ? (char) i : customCharsArray[i - 256];
//
//                // create image
//                BufferedImage fontImage = DrawableUtils.getFontImage(ch,
//                        this.antiAlias, this.font, this.fontSize, c);
//
//                // inc height if needed
//                this.fontHeight =
//                        Math.max(this.fontHeight, fontImage.getHeight());
//
//                if (i < 256) {
//                    // standard characters
//                    this.charArray[i] = new VBAOChar(fontImage);
//                } else {
//                    // custom characters
//                    this.customChars.put(new Character(ch),
//                            new VBAOChar(fontImage));
//                }
//            }
//
//        } catch (Exception e) {
//            LUtils.print("Failed to create font.");
//            e.printStackTrace();
//        }
//    }
//
//    private void drawQuad(double xPos, double yPos, double scaleX,
//            double scaleY, VBAOChar vchar) {
//        glBeginTrans(xPos, yPos, 0);
//        glBeginScale(scaleX, scaleY, 1);
//        vchar.quad.draw();
//        glEndScale();
//        glEndTrans();
//    }
//
//    public int getWidth(String whatchars) {
//        int totalwidth = 0;
//        int currentChar = 0;
//        for (int i = 0; i < whatchars.length(); i++) {
//            currentChar = whatchars.charAt(i);
//            VBAOChar vchar = (currentChar < 256) ? this.charArray[currentChar]
//                    : this.customChars.get(new Character((char) currentChar));
//
//            if (vchar != null)
//                totalwidth += vchar.tex.dim.width;
//        }
//        return totalwidth;
//    }
//
//    public int getHeight() {
//        return this.fontHeight;
//    }
//
//    public int getHeight(String HeightString) {
//        return this.fontHeight * Strings.count(HeightString, '\n');
//    }
//
//    public int getLineHeight() {
//        return this.fontHeight;
//    }
//
//    public void drawString(double x, double y, String string, double scaleX,
//            double scaleY) {
//        drawString(x, y, string, 0, string.length() - 1, scaleX, scaleY,
//                ALIGN_LEFT);
//    }
//
//    public void drawString(double x, double y, String string, double scaleX,
//            double scaleY, int format) {
//        drawString(x, y, string, 0, string.length() - 1, scaleX, scaleY,
//                format);
//    }
//
//    public void drawString(double x, double y, String string, int startIndex,
//            int endIndex, double scaleX, double scaleY, int format) {
//
//        VBAOChar vchar = null;
//        int charCurrent;
//
//        int totalwidth = 0;
//        int i = startIndex, d, c;
//        double startY = 0;
//
//        switch (format) {
//            case ALIGN_RIGHT: {
//                d = -1;
//                c = this.correctR;
//
//                while (i < endIndex) {
//                    if (string.charAt(i) == '\n')
//                        startY -= this.fontHeight;
//                    i++;
//                }
//                break;
//            }
//            case ALIGN_CENTER: {
//                for (int l = startIndex; l <= endIndex; l++) {
//                    charCurrent = string.charAt(l);
//                    if (charCurrent == '\n')
//                        break;
//                    if (charCurrent < 256) {
//                        vchar = this.charArray[charCurrent];
//                    } else {
//                        vchar = this.customChars
//                                .get(new Character((char) charCurrent));
//                    }
//                    totalwidth += vchar.tex.dim.width - this.correctL;
//                }
//                totalwidth /= -2;
//            }
//            case ALIGN_LEFT:
//            default: {
//                d = 1;
//                c = this.correctL;
//                break;
//            }
//
//        }
//
//        while (i >= startIndex && i <= endIndex) {
//
//            charCurrent = string.charAt(i);
//            if (charCurrent < 256) {
//                vchar = this.charArray[charCurrent];
//            } else {
//                vchar = this.customChars.get(new Character((char) charCurrent));
//            }
//
//            if (vchar != null) {
//                if (d < 0)
//                    totalwidth += (vchar.tex.dim.width - c) * d;
//                if (charCurrent == '\n') {
//                    startY -= this.fontHeight * d;
//                    totalwidth = 0;
//                    if (format == ALIGN_CENTER) {
//                        for (int l = i + 1; l <= endIndex; l++) {
//                            charCurrent = string.charAt(l);
//                            if (charCurrent == '\n')
//                                break;
//                            if (charCurrent < 256) {
//                                vchar = this.charArray[charCurrent];
//                            } else {
//                                vchar = this.customChars
//                                        .get(new Character((char) charCurrent));
//                            }
//                            totalwidth += vchar.tex.dim.width - this.correctL;
//                        }
//                        totalwidth /= -2;
//                    }
//                    // if center get next lines total width/2;
//                } else {
//                    drawQuad(totalwidth + x, startY + y, scaleX, scaleY, vchar);
//                    if (d > 0)
//                        totalwidth += (vchar.tex.dim.width - c) * d;
//                }
//                i += d;
//
//            }
//        }
//    }
//
//    public static byte[] intToByteArray(int value) {
//        return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
//                (byte) (value >>> 8), (byte) value };
//    }
//
//    public void destroy() {
//        System.err.println("// Cleaning Textures \\\\");
//        VBAOChar[] all =
//                new VBAOChar[this.charArray.length + this.customChars.size()];
//        System.arraycopy(this.charArray, 0, all, 0, this.charArray.length);
//        if (this.customChars.size() != 0) {
//            VBAOChar[] c =
//                    this.customChars.values().stream().toArray(VBAOChar[]::new);
//            System.arraycopy(c, 0, all, this.charArray.length, c.length);
//        }
//        for (VBAOChar vchar : all) {
//            vchar.destroy();
//        }
//        System.err.println("\\\\      Complete     //");
//    }
//}
