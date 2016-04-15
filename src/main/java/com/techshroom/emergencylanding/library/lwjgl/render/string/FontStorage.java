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
                        throw new IllegalArgumentException(
                                "Don't know what style " + style + " is.");
                    }
                });
                return buffer.toString();
            }
        }

        public static FontRenderingData create(String fontId,
                Supplier<InputStream> fontData, int fontSize, Style... style) {
            return create(fontId, fontData, fontSize,
                    ImmutableSet.copyOf(style));
        }

        public static FontRenderingData create(String fontId,
                Supplier<InputStream> fontData, int fontSize, Style style) {
            return create(fontId, fontData, fontSize, ImmutableSet.of(style));
        }

        public static FontRenderingData create(String fontId,
                Supplier<InputStream> fontData, int fontSize, Style styleA,
                Style styleB) {
            return create(fontId, fontData, fontSize,
                    ImmutableSet.of(styleA, styleB));
        }

        public static FontRenderingData create(String fontId,
                Supplier<InputStream> fontData, int fontSize) {
            return create(fontId, fontData, fontSize, ImmutableSet.of());
        }

        public static FontRenderingData create(String fontId,
                Supplier<InputStream> fontData, int fontSize,
                Collection<Style> style) {
            return new AutoValue_FontStorage_FontRenderingData(fontData,
                    ImmutableSet.copyOf(style), fontId + Style.stringify(style),
                    fontSize);
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
                FontStorage.FontRenderingData that =
                        (FontStorage.FontRenderingData) o;
                return (getStyle().equals(that.getStyle()))
                        && (getFontIdentifier()
                                .equals(that.getFontIdentifier()))
                        && (getFontSize() == that.getFontSize());
            }
            return false;
        }

    }

    private static final LoadingCache<FontRenderingData, StringRenderer> RENDER_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES)
                    .maximumWeight(3041937L /* sizeof new instance */ * 1024)
                    .weigher(new StringRenderer.CacheWeigher())
                    .ticker(GLFWTicker.INSTANCE)
                    .removalListener(StringRenderer::onRemoval)
                    .build(new CacheLoader<FontRenderingData, StringRenderer>() {

                        @Override
                        public StringRenderer load(FontRenderingData key)
                                throws Exception {
                            return new StringRenderer(key);
                        }
                    });

    private FontStorage() {
        throw new AssertionError("NO.");
    }

}
