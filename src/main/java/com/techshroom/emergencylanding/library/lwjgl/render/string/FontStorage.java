package com.techshroom.emergencylanding.library.lwjgl.render.string;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.auto.value.AutoValue;
import com.google.common.base.Supplier;
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
        }

        public static FontRenderingData create(
                Supplier<SeekableByteChannel> fontPath, int fontSize,
                Style... style) {
            return create(fontPath, fontSize, ImmutableSet.copyOf(style));
        }

        public static FontRenderingData create(
                Supplier<SeekableByteChannel> fontPath, int fontSize,
                Style style) {
            return create(fontPath, fontSize, ImmutableSet.of(style));
        }

        public static FontRenderingData create(
                Supplier<SeekableByteChannel> fontPath, int fontSize,
                Style styleA, Style styleB) {
            return create(fontPath, fontSize, ImmutableSet.of(styleA, styleB));
        }

        public static FontRenderingData
                create(Supplier<SeekableByteChannel> fontPath, int fontSize) {
            return create(fontPath, fontSize, ImmutableSet.of());
        }

        public static FontRenderingData create(
                Supplier<SeekableByteChannel> fontPath, int fontSize,
                Collection<Style> style) {
            return new AutoValue_FontStorage_FontRenderingData(fontPath,
                    ImmutableSet.copyOf(style), fontSize);
        }

        public abstract Supplier<SeekableByteChannel> getInputStream();

        public abstract ImmutableSet<Style> getStyle();

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

    }

    private static final LoadingCache<FontRenderingData, StringRenderer> RENDER_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES)
                    .maximumWeight(10 * 1024 * 1024)
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
