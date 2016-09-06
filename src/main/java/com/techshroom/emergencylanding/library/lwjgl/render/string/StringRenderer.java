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
package com.techshroom.emergencylanding.library.lwjgl.render.string;

import static org.lwjgl.nanovg.NanoVG.nvgCreateFontMem;
import static org.lwjgl.nanovg.NanoVG.nvgFontFaceId;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgTextBox;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryUtil;

import com.flowpowered.math.vector.Vector2f;
import com.google.common.cache.RemovalNotification;
import com.google.common.cache.Weigher;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.render.string.FontStorage.FontRenderingData;
import com.techshroom.emergencylanding.library.util.LUtils;

/**
 * API for rendering a string.
 * 
 * @author Kenzie Togami (kenzierocks)
 */
public class StringRenderer {

    /**
     * Weighs a {@link StringRenderer} by memory size.
     */
    static final class CacheWeigher implements Weigher<FontRenderingData, StringRenderer> {

        @Override
        public int weigh(FontRenderingData key, StringRenderer value) {
            // int space = Maths.addWithOverflowChecks(
            // value.storedCodePoints.cardinality(),
            // value.fontData.limit(), value.pixels.limit());
            // System.err.println("Taking up " + space);
            // return space;
            return 0;
        }

    }

    static void onRemoval(RemovalNotification<FontRenderingData, StringRenderer> rm) {
        rm.getValue().destroy();
    }

    private final FontStorage.FontRenderingData data;
    private long nvgHandle;
    // keeping fontData around for NanoVG
    @SuppressWarnings("unused")
    private ByteBuffer fontData;
    private int fontMem;

    StringRenderer(FontStorage.FontRenderingData data) throws IOException {
        this.data = data;
        this.nvgHandle = DisplayLayer.getForContext().getNvgHandle();
        this.fontMem = nvgCreateFontMem(this.nvgHandle, data.getFontIdentifier(),
                this.fontData = LUtils.inputStreamToDirectByteBuffer(data.getInputStream()), 1);
    }

    private void destroy() {
        this.nvgHandle = 0;
        this.fontData = null;
        this.fontMem = 0;
    }

    public void renderString(String string, Vector2f position) {
        nvgFontFaceId(this.nvgHandle, this.fontMem);
        nvgFontSize(this.nvgHandle, this.data.getFontSize());
        nvgTextBox(this.nvgHandle, position.getX(), position.getY(), 1e5f, string, MemoryUtil.NULL);
    }

}
