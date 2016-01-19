/* 
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.techshroom.emergencylanding.imported;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * A mutable Color class
 * 
 * @author $Author$
 * @version $Revision$ $Id$
 */
public final class Color implements Serializable {

    /**
     * The color white.
     */
    public final static Color WHITE = new Color(255, 255, 255);

    /**
     * The color light gray.
     */
    public final static Color LIGHT_GRAY = new Color(192, 192, 192);

    /**
     * The color gray.
     */
    public final static Color GRAY = new Color(128, 128, 128);

    /**
     * The color dark gray.
     */
    public final static Color DARK_GRAY = new Color(64, 64, 64);

    /**
     * The color black.
     */
    public final static Color BLACK = new Color(0, 0, 0);

    /**
     * The color red.
     */
    public final static Color RED = new Color(255, 0, 0);

    /**
     * The color pink.
     */
    public final static Color PINK = new Color(255, 175, 175);

    /**
     * The color orange.
     */
    public final static Color ORANGE = new Color(255, 200, 0);

    /**
     * The color yellow.
     */
    public final static Color YELLOW = new Color(255, 255, 0);

    /**
     * The color green.
     */
    public final static Color GREEN = new Color(0, 255, 0);

    /**
     * The color magenta.
     */
    public final static Color MAGENTA = new Color(255, 0, 255);

    /**
     * The color cyan.
     */
    public final static Color CYAN = new Color(0, 255, 255);

    /**
     * The color blue.
     */
    public final static Color BLUE = new Color(0, 0, 255);

    static final long serialVersionUID = 1L;

    /** Color components, publicly accessible */
    private byte red, green, blue, alpha;

    /**
     * Constructor for Color.
     */
    public Color() {
        this(0, 0, 0, 255);
    }

    /**
     * Constructor for Color. Alpha defaults to 255.
     */
    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    /**
     * Constructor for Color. Alpha defaults to 255.
     */
    public Color(byte r, byte g, byte b) {
        this(r, g, b, (byte) 255);
    }

    /**
     * Constructor for Color.
     */
    public Color(int r, int g, int b, int a) {
        set(r, g, b, a);
    }

    /**
     * Constructor for Color.
     */
    public Color(byte r, byte g, byte b, byte a) {
        set(r, g, b, a);
    }

    /**
     * Set a color
     */
    public void set(int r, int g, int b, int a) {
        this.red = (byte) r;
        this.green = (byte) g;
        this.blue = (byte) b;
        this.alpha = (byte) a;
    }

    /**
     * Set a color
     */
    public void set(byte r, byte g, byte b, byte a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    /**
     * Set a color
     */
    public void set(int r, int g, int b) {
        set(r, g, b, 255);
    }

    /**
     * Set a color
     */
    public void set(byte r, byte g, byte b) {
        set(r, g, b, (byte) 255);
    }

    /**
     * Accessor
     */
    public int getRed() {
        return this.red & 0xFF;
    }

    /**
     * Accessor
     */
    public int getGreen() {
        return this.green & 0xFF;
    }

    /**
     * Accessor
     */
    public int getBlue() {
        return this.blue & 0xFF;
    }

    /**
     * Accessor
     */
    public int getAlpha() {
        return this.alpha & 0xFF;
    }

    /**
     * Set the Red component
     */
    public void setRed(int red) {
        this.red = (byte) red;
    }

    /**
     * Set the Green component
     */
    public void setGreen(int green) {
        this.green = (byte) green;
    }

    /**
     * Set the Blue component
     */
    public void setBlue(int blue) {
        this.blue = (byte) blue;
    }

    /**
     * Set the Alpha component
     */
    public void setAlpha(int alpha) {
        this.alpha = (byte) alpha;
    }

    /**
     * Set the Red component
     */
    public void setRed(byte red) {
        this.red = red;
    }

    /**
     * Set the Green component
     */
    public void setGreen(byte green) {
        this.green = green;
    }

    /**
     * Set the Blue component
     */
    public void setBlue(byte blue) {
        this.blue = blue;
    }

    /**
     * Set the Alpha component
     */
    public void setAlpha(byte alpha) {
        this.alpha = alpha;
    }

    /**
     * Stringify
     */
    @Override
    public String toString() {
        return "Color [" + getRed() + ", " + getGreen() + ", " + getBlue()
                + ", " + getAlpha() + "]";
    }

    /**
     * Equals
     */
    @Override
    public boolean equals(Object o) {
        return (o != null) && (o instanceof Color)
                && (((Color) o).getRed() == this.getRed())
                && (((Color) o).getGreen() == this.getGreen())
                && (((Color) o).getBlue() == this.getBlue())
                && (((Color) o).getAlpha() == this.getAlpha());
    }

    /**
     * Hashcode
     */
    @Override
    public int hashCode() {
        return (this.red << 24) | (this.green << 16) | (this.blue << 8)
                | this.alpha;
    }

    /*
     * (Overrides)
     * 
     * @see com.shavenpuppy.jglib.ReadableColor#getAlphaByte()
     */
    public byte getAlphaByte() {
        return this.alpha;
    }

    /*
     * (Overrides)
     * 
     * @see com.shavenpuppy.jglib.ReadableColor#getBlueByte()
     */
    public byte getBlueByte() {
        return this.blue;
    }

    /*
     * (Overrides)
     * 
     * @see com.shavenpuppy.jglib.ReadableColor#getGreenByte()
     */
    public byte getGreenByte() {
        return this.green;
    }

    /*
     * (Overrides)
     * 
     * @see com.shavenpuppy.jglib.ReadableColor#getRedByte()
     */
    public byte getRedByte() {
        return this.red;
    }

    /*
     * (Overrides)
     * 
     * @see com.shavenpuppy.jglib.ReadableColor#writeRGBA(java.nio.ByteBuffer)
     */
    public void writeRGBA(ByteBuffer dest) {
        dest.put(this.red);
        dest.put(this.green);
        dest.put(this.blue);
        dest.put(this.alpha);
    }

    /*
     * (Overrides)
     * 
     * @see com.shavenpuppy.jglib.ReadableColor#writeRGB(java.nio.ByteBuffer)
     */
    public void writeRGB(ByteBuffer dest) {
        dest.put(this.red);
        dest.put(this.green);
        dest.put(this.blue);
    }

    /*
     * (Overrides)
     * 
     * @see com.shavenpuppy.jglib.ReadableColor#writeABGR(java.nio.ByteBuffer)
     */
    public void writeABGR(ByteBuffer dest) {
        dest.put(this.alpha);
        dest.put(this.blue);
        dest.put(this.green);
        dest.put(this.red);
    }

    /*
     * (Overrides)
     * 
     * @see com.shavenpuppy.jglib.ReadableColor#writeARGB(java.nio.ByteBuffer)
     */
    public void writeARGB(ByteBuffer dest) {
        dest.put(this.alpha);
        dest.put(this.red);
        dest.put(this.green);
        dest.put(this.blue);
    }

    /*
     * (Overrides)
     * 
     * @see com.shavenpuppy.jglib.ReadableColor#writeBGR(java.nio.ByteBuffer)
     */
    public void writeBGR(ByteBuffer dest) {
        dest.put(this.blue);
        dest.put(this.green);
        dest.put(this.red);
    }

    /*
     * (Overrides)
     * 
     * @see com.shavenpuppy.jglib.ReadableColor#writeBGRA(java.nio.ByteBuffer)
     */
    public void writeBGRA(ByteBuffer dest) {
        dest.put(this.blue);
        dest.put(this.green);
        dest.put(this.red);
        dest.put(this.alpha);
    }

    /**
     * Read a color from a byte buffer
     * 
     * @param src
     *            The source buffer
     */
    public void readRGBA(ByteBuffer src) {
        this.red = src.get();
        this.green = src.get();
        this.blue = src.get();
        this.alpha = src.get();
    }

    /**
     * Read a color from a byte buffer
     * 
     * @param src
     *            The source buffer
     */
    public void readRGB(ByteBuffer src) {
        this.red = src.get();
        this.green = src.get();
        this.blue = src.get();
    }

    /**
     * Read a color from a byte buffer
     * 
     * @param src
     *            The source buffer
     */
    public void readARGB(ByteBuffer src) {
        this.alpha = src.get();
        this.red = src.get();
        this.green = src.get();
        this.blue = src.get();
    }

    /**
     * Read a color from a byte buffer
     * 
     * @param src
     *            The source buffer
     */
    public void readBGRA(ByteBuffer src) {
        this.blue = src.get();
        this.green = src.get();
        this.red = src.get();
        this.alpha = src.get();
    }

    /**
     * Read a color from a byte buffer
     * 
     * @param src
     *            The source buffer
     */
    public void readBGR(ByteBuffer src) {
        this.blue = src.get();
        this.green = src.get();
        this.red = src.get();
    }

    /**
     * Read a color from a byte buffer
     * 
     * @param src
     *            The source buffer
     */
    public void readABGR(ByteBuffer src) {
        this.alpha = src.get();
        this.blue = src.get();
        this.green = src.get();
        this.red = src.get();
    }

    /**
     * Set this color's color by copying another color
     * 
     * @param src
     *            The source color
     */
    public void setColor(Color src) {
        this.red = src.getRedByte();
        this.green = src.getGreenByte();
        this.blue = src.getBlueByte();
        this.alpha = src.getAlphaByte();
    }

    /**
     * HSB to RGB conversion, pinched from java.awt.Color.
     * 
     * @param hue
     *            (0..1.0f)
     * @param saturation
     *            (0..1.0f)
     * @param brightness
     *            (0..1.0f)
     */
    public void fromHSB(float hue, float saturation, float brightness) {
        if (saturation == 0.0F) {
            this.red =
                    this.green = this.blue = (byte) (brightness * 255F + 0.5F);
        } else {
            float f3 = (hue - (float) Math.floor(hue)) * 6F;
            float f4 = f3 - (float) Math.floor(f3);
            float f5 = brightness * (1.0F - saturation);
            float f6 = brightness * (1.0F - saturation * f4);
            float f7 = brightness * (1.0F - saturation * (1.0F - f4));
            switch ((int) f3) {
                case 0:
                    this.red = (byte) (brightness * 255F + 0.5F);
                    this.green = (byte) (f7 * 255F + 0.5F);
                    this.blue = (byte) (f5 * 255F + 0.5F);
                    break;
                case 1:
                    this.red = (byte) (f6 * 255F + 0.5F);
                    this.green = (byte) (brightness * 255F + 0.5F);
                    this.blue = (byte) (f5 * 255F + 0.5F);
                    break;
                case 2:
                    this.red = (byte) (f5 * 255F + 0.5F);
                    this.green = (byte) (brightness * 255F + 0.5F);
                    this.blue = (byte) (f7 * 255F + 0.5F);
                    break;
                case 3:
                    this.red = (byte) (f5 * 255F + 0.5F);
                    this.green = (byte) (f6 * 255F + 0.5F);
                    this.blue = (byte) (brightness * 255F + 0.5F);
                    break;
                case 4:
                    this.red = (byte) (f7 * 255F + 0.5F);
                    this.green = (byte) (f5 * 255F + 0.5F);
                    this.blue = (byte) (brightness * 255F + 0.5F);
                    break;
                case 5:
                    this.red = (byte) (brightness * 255F + 0.5F);
                    this.green = (byte) (f5 * 255F + 0.5F);
                    this.blue = (byte) (f6 * 255F + 0.5F);
                    break;
            }
        }
    }

    /**
     * RGB to HSB conversion, pinched from java.awt.Color. The HSB value is
     * returned in dest[] if dest[] is supplied. Values range from 0..1
     * 
     * @param dest
     *            Destination floats, or null
     * @return dest, or a new float array
     */
    public float[] toHSB(float dest[]) {
        int r = getRed();
        int g = getGreen();
        int b = getBlue();
        if (dest == null)
            dest = new float[3];
        int l = r <= g ? g : r;
        if (b > l)
            l = b;
        int i1 = r >= g ? g : r;
        if (b < i1)
            i1 = b;
        float brightness = l / 255F;
        float saturation;
        if (l != 0)
            saturation = (float) (l - i1) / (float) l;
        else
            saturation = 0.0F;
        float hue;
        if (saturation == 0.0F) {
            hue = 0.0F;
        } else {
            float f3 = (float) (l - r) / (float) (l - i1);
            float f4 = (float) (l - g) / (float) (l - i1);
            float f5 = (float) (l - b) / (float) (l - i1);
            if (r == l)
                hue = f5 - f4;
            else if (g == l)
                hue = (2.0F + f3) - f5;
            else
                hue = (4F + f4) - f3;
            hue /= 6F;
            if (hue < 0.0F)
                hue++;
        }
        dest[0] = hue;
        dest[1] = saturation;
        dest[2] = brightness;
        return dest;
    }

}
