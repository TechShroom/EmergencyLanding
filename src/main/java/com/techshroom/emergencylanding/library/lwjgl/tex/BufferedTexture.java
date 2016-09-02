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
package com.techshroom.emergencylanding.library.lwjgl.tex;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.Platform;

import com.flowpowered.math.vector.Vector2i;
import com.techshroom.emergencylanding.library.util.LUtils;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class BufferedTexture extends ELTexture {

    private static final String REGEX_DOUBLE_WINDOWS_SEPERATOR = File.separator.replace("\\", "\\\\");
    private static final String ENDS_WITH_DOUBLE_WINDOWS_SEPEARATOR = "^(.+)" + REGEX_DOUBLE_WINDOWS_SEPERATOR + "$",
            STARTS_WITH_DOUBLE_WINDOWS_SEPERATOR = "^" + REGEX_DOUBLE_WINDOWS_SEPERATOR + "(.+)$";

    public static BufferedTexture generateTexture(String parentDir, String name) {
        if (parentDir == null) {
            parentDir =
                    System.getProperty("user.home", (Platform.get() == Platform.WINDOWS ? "C:" : "") + File.separator);
        }
        if (name == null) {
            throw new IllegalStateException("name cannot be null");
        }
        try {
            return LUtils
                    .processPathData(parentDir
                            + ((parentDir.matches(ENDS_WITH_DOUBLE_WINDOWS_SEPEARATOR)
                                    || name.matches(STARTS_WITH_DOUBLE_WINDOWS_SEPERATOR)) ? "" : File.separator)
                            + name, BufferedTexture::fromInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error retriving stream", e);
        }
    }

    public static BufferedTexture fromInputStream(InputStream stream) throws IOException {
        // Link the PNG decoder to this stream
        PNGDecoder decoder = new PNGDecoder(stream);

        // Decode the PNG file in a ByteBuffer
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
        buf.flip();
        return new BufferedTexture(buf, decoder.getWidth(), decoder.getHeight());
    }

    private final ByteBuffer sourceBuffer;

    public BufferedTexture(byte[] image, int width, int height) {
        this(ByteBuffer.wrap(image), width, height);
    }

    public BufferedTexture(ByteBuffer image, int width, int height) {
        this.dim = new Vector2i(width, height);
        // We can't assume the image is a direct buffer.
        // Save it for later!
        this.sourceBuffer = image;
        super.init();
    }

    @Override
    public void setup() {
        this.sourceBuffer.mark();
        this.buf = BufferUtils.createByteBuffer(this.sourceBuffer.capacity());
        this.buf.put(this.sourceBuffer);
        this.sourceBuffer.reset();
        this.buf.flip();
    }

    @Override
    public boolean isLookAlike(ELTexture t) {
        if (t instanceof BufferedTexture) {
            BufferedTexture other = (BufferedTexture) t;
            if (other.dim.equals(this.dim)) {
                return this.sourceBuffer.equals(other.sourceBuffer);
            }
        }
        return false;
    }

    @Override
    protected void onDestruction() {
    }
}
