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
package com.techshroom.emergencylanding.library.lwjgl.tex;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;

import com.flowpowered.math.vector.Vector2i;
import com.techshroom.emergencylanding.imported.Color;

public class ColorTexture extends ELTexture {

    private static final Vector2i SIZE_1 = new Vector2i(1, 1);
    private static final Map<Color, ColorTexture> COLOR_CACHE = new HashMap<>();
    private final Color c;

    public ColorTexture(Color from, Vector2i size) {
        this.c = from;
        this.dim = size;
        super.init();
    }

    public ColorTexture(Color from) {
        this(from, SIZE_1);
    }

    @Override
    public void setup() {
        this.buf = BufferUtils
                .createByteBuffer(4 * this.dim.getX() * this.dim.getY());
        for (int i = 0; i < this.buf.capacity(); i += 4) {
            this.c.writeRGBA(this.buf);
        }
    }

    @Override
    public boolean isLookAlike(ELTexture t) {
        if (t instanceof ColorTexture) {
            return this.c.equals(((ColorTexture) t).c)
                    && this.dim.equals(t.dim);
        } else {
            return super.isLookAlike(t);
        }
    }

    public Color getRawColor() {
        return this.c;
    }

    public static ColorTexture getColor(Color color) {
        return COLOR_CACHE.computeIfAbsent(color, ColorTexture::new);
    }

    @Override
    protected void onDestruction() {
        COLOR_CACHE.remove(this.c);
    }

}
