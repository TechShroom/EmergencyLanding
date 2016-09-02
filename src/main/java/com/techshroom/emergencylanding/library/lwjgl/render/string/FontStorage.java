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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import com.google.auto.value.AutoValue;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.techshroom.emergencylanding.library.util.GLFWTicker;

public final class FontStorage {

    @AutoValue
    public static abstract class FontRenderingData {

        public enum Style {
            BOLD, ITALIC;

            public static String stringify(Collection<Style> styles) {
                StringBuffer buffer = new StringBuffer();
                styles.stream().distinct().forEach(style -> {
                    if (style == BOLD) {
                        buffer.append("-bold");
                    } else if (style == ITALIC) {
                        buffer.append("-italic");
                    } else {
                        throw new IllegalArgumentException("Don't know what style " + style + " is.");
                    }
                });
                return buffer.toString();
            }
        }

        public static FontRenderingData
                create(String fontId, Supplier<InputStream> fontData, int fontSize, Style... style) {
            return create(fontId, fontData, fontSize, ImmutableSet.copyOf(style));
        }

        public static FontRenderingData
                create(String fontId, Supplier<InputStream> fontData, int fontSize, Style style) {
            return create(fontId, fontData, fontSize, ImmutableSet.of(style));
        }

        public static FontRenderingData
                create(String fontId, Supplier<InputStream> fontData, int fontSize, Style styleA, Style styleB) {
            return create(fontId, fontData, fontSize, ImmutableSet.of(styleA, styleB));
        }

        public static FontRenderingData create(String fontId, Supplier<InputStream> fontData, int fontSize) {
            return create(fontId, fontData, fontSize, ImmutableSet.of());
        }

        public static FontRenderingData
                create(String fontId, Supplier<InputStream> fontData, int fontSize, Collection<Style> style) {
            return new AutoValue_FontStorage_FontRenderingData(fontData, ImmutableSet.copyOf(style),
                    fontId + Style.stringify(style), fontSize);
        }

        public abstract Supplier<InputStream> getInputStream();

        public abstract ImmutableSet<Style> getStyle();

        public abstract String getFontIdentifier();

        public abstract int getFontSize();

        public StringRenderer getStringRenderer() throws IOException {
            try {
                return RENDER_CACHE.get(this);
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                Throwables.propagateIfInstanceOf(cause, IOException.class);
                throw Throwables.propagate(cause);
            }
        }

        @Override
        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof FontStorage.FontRenderingData) {
                FontStorage.FontRenderingData that = (FontStorage.FontRenderingData) o;
                return (getStyle().equals(that.getStyle())) && (getFontIdentifier().equals(that.getFontIdentifier()))
                        && (getFontSize() == that.getFontSize());
            }
            return false;
        }

    }

    private static final LoadingCache<FontRenderingData, StringRenderer> RENDER_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES)
                    .maximumWeight(3041937L /* sizeof new instance */ * 1024).weigher(new StringRenderer.CacheWeigher())
                    .ticker(GLFWTicker.INSTANCE).removalListener(StringRenderer::onRemoval)
                    .build(new CacheLoader<FontRenderingData, StringRenderer>() {

                        @Override
                        public StringRenderer load(FontRenderingData key) throws Exception {
                            return new StringRenderer(key);
                        }
                    });

    private FontStorage() {
        throw new AssertionError("NO.");
    }

}
