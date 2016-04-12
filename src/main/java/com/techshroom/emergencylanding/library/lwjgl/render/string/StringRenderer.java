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
package com.techshroom.emergencylanding.library.lwjgl.render.string;

import static com.google.common.base.Preconditions.checkArgument;
import static org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glGetInteger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackRange;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Pointer;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector2i;
import com.google.common.base.Supplier;
import com.google.common.cache.RemovalNotification;
import com.google.common.cache.Weigher;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeSet;
import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.lwjgl.render.VBAO;
import com.techshroom.emergencylanding.library.lwjgl.render.VertexData;
import com.techshroom.emergencylanding.library.lwjgl.render.string.FontStorage.FontRenderingData;
import com.techshroom.emergencylanding.library.shapeup.Rectangle;
import com.techshroom.emergencylanding.library.util.DeallocatedException;
import com.techshroom.emergencylanding.library.util.Maths;

/**
 * API for {@link STBTruetype}.
 * 
 * @author Kenzie Togami (kenzierocks)
 */
public class StringRenderer {

    private static final int MAX_CHARS_PER_TEXTURE = 1024;
    // Chars per row
    private static final int FONT_TO_REGULAR_WIDTH = 100;
    // Chars per col
    private static final int FONT_TO_REGULAR_HEIGHT = 100;
    private static final Range<Integer> ASCII_RANGE = Range.closedOpen(0, 256);

    /**
     * Weighs a {@link StringRenderer} by memory size.
     */
    static final class CacheWeigher
            implements Weigher<FontRenderingData, StringRenderer> {

        @Override
        public int weigh(FontRenderingData key, StringRenderer value) {
            int space = Maths.addWithOverflowChecks(
                    value.storedCodePoints.cardinality(),
                    value.fontData.limit(), value.pixels.limit());
            // System.err.println("Taking up " + space);
            return space;
        }

    }

    private static ByteBuffer loadFont(Supplier<SeekableByteChannel> font)
            throws IOException {
        ByteBuffer buffer;

        try (SeekableByteChannel fc = font.get()) {
            buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);

            while (fc.read(buffer) != -1) {
            }
        }

        buffer.flip();
        return buffer;
    }

    static void onRemoval(
            RemovalNotification<FontRenderingData, StringRenderer> rm) {
        rm.getValue().destroy();
    }

    private final BitSet storedCodePoints = new BitSet();
    // Use number of ascii chars. We know we'll always hold that much.
    private final Map<Integer, Integer> codePointToCharDataIndex =
            new HashMap<>(
                    ASCII_RANGE.upperEndpoint() - ASCII_RANGE.lowerEndpoint());
    private final STBTTPackedchar.Buffer charData;
    private int lastUsedBufferIndex = -1;
    private final ByteBuffer fontData;
    private final STBTTPackContext context = STBTTPackContext.create();
    private final ByteBuffer pixels;
    private final StringTexture pixelsTexture;
    private final float fontHeight;
    private final int width;
    private final int height;

    StringRenderer(FontStorage.FontRenderingData data) throws IOException {
        STBTTFontinfo fontInfo = STBTTFontinfo.create();
        STBTruetype.stbtt_InitFont(fontInfo,
                this.fontData = loadFont(data.getInputStream()));
        this.fontHeight = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo,
                data.getFontSize());
        int maxTexSize = glGetInteger(GL_MAX_TEXTURE_SIZE);
        this.width = Math.min(
                (int) ((data.getFontSize() * 0.5) * FONT_TO_REGULAR_WIDTH) + 1,
                maxTexSize);
        this.height = Math.min(
                (int) (data.getFontSize() * FONT_TO_REGULAR_HEIGHT) + 1,
                maxTexSize);
        System.err.println(new Vector2i(this.width, this.height));
        this.pixels = BufferUtils.createByteBuffer(this.width * this.height);
        this.pixelsTexture =
                new StringTexture(this.pixels, this.width, this.height);
        // Can probably assume a certain amount of characters.
        // TODO fine tune.
        this.charData = STBTTPackedchar.calloc(MAX_CHARS_PER_TEXTURE);
        if (STBTruetype.stbtt_PackBegin(this.context, this.pixels, this.width,
                this.height, 0, 1) == 0) {
            throw new IllegalStateException("Failed to load font");
        }
        // STBTruetype.stbtt_PackSetOversampling(this.context, 2, 2);
        packAsciiRange();
    }

    private void packAsciiRange() {
        packCodePointRange(ASCII_RANGE);
    }

    public void packCodePointRange(Range<Integer> range) {
        packCodePointRanges(Stream.of(range));
    }

    @SafeVarargs
    public final void packCodePointRanges(Range<Integer>... ranges) {
        packCodePointRanges(Stream.of(ranges));
    }

    public void packCodePointRanges(Stream<Range<Integer>> ranges) {
        List<Range<Integer>> rangeList =
                ranges.collect(Collectors.toCollection(ArrayList::new));
        List<? extends Set<Integer>> rangeSets = ImmutableList
                .copyOf(rangeList.stream().flatMap(this::getOnlyNotAddedRanges)
                        .map(r -> ContiguousSet.create(r,
                                DiscreteDomain.integers()))::iterator);
        packCodePointRangesFinal(rangeSets);
    }

    public void packCodePointRanges(Set<Integer> codePoints) {
        if (codePoints.isEmpty()) {
            return;
        }
        packCodePointRangesFinal(ImmutableList.of(codePoints));
    }

    private void
            packCodePointRangesFinal(List<? extends Set<Integer>> rangeList) {
        int[] offsetIntoChardata = new int[1];
        STBTTPackRange.Buffer stbRanges =
                convertRangeListToSTBTTRanges(rangeList, offsetIntoChardata);
        if (STBTruetype.stbtt_PackFontRanges(this.context, this.fontData, 0,
                stbRanges) == 0) {
            throw new IllegalArgumentException(
                    "Failed to PackFontRanges on " + rangeList);
        }
        System.err.println(MemoryUtil.memAddress(this.pixels));
        System.err.println(MemoryUtil.memGetLong(this.context.address()
                + Pointer.POINTER_SIZE * 2 + 6 * Integer.BYTES));
        for (int i = 0; i < stbRanges.limit(); i++) {
            STBTTPackRange range = stbRanges.get(i);
            STBTTPackedchar.Buffer chars = range.chardata_for_range();
            IntBuffer data = range.array_of_unicode_codepoints();
            for (int j = 0; j < chars.limit(); j++) {
                STBTTPackedchar ch = chars.get(j);
                this.pixelsTexture.onUpdatedPixels(
                        new Vector2d(ch.x0(), ch.y0()),
                        Rectangle.fromLengthAndWidth(ch.x1() - ch.x0(),
                                ch.y1() - ch.y0()));
                int codePoint = data.get(j);
                this.codePointToCharDataIndex.put(codePoint,
                        offsetIntoChardata[0] + j);
            }
        }
        rangeList.stream().flatMap(Set::stream)
                .forEach(this.storedCodePoints::set);
    }

    private STBTTPackRange.Buffer convertRangeListToSTBTTRanges(
            List<? extends Set<Integer>> rangeList, int[] offsetIntoCharData) {
        offsetIntoCharData[0] = this.lastUsedBufferIndex + 1;
        STBTTPackRange.Buffer buffer = STBTTPackRange.create(rangeList.size());
        for (int i = 0; i < rangeList.size(); i++) {
            Set<Integer> intSet = rangeList.get(i);
            int intSizes = intSet.size();
            if (intSizes == 0) {
                // Skip it for performance
                continue;
            }
            for (Integer codePoint : intSet) {
                checkArgument(Character.isValidCodePoint(codePoint),
                        "Code points must be valid");
            }
            STBTTPackRange stbRange = buffer.get(i);

            stbRange.font_size(this.fontHeight);

            stbRange.num_chars(intSizes);

            IntBuffer data = BufferUtils.createIntBuffer(intSizes);
            data.put(intSet.stream().mapToInt(Integer::intValue).toArray());
            data.flip();
            stbRange.array_of_unicode_codepoints(data);

            // Get next slice.
            int start = this.lastUsedBufferIndex + 1;
            int end = this.lastUsedBufferIndex = start + intSizes;
            STBTTPackedchar.Buffer subBufferView = STBTTPackedchar
                    .create(this.charData.address(start), start - end);
            stbRange.chardata_for_range(subBufferView);
        }
        // buffer.flip();
        return buffer;
    }

    private Stream<Range<Integer>> getOnlyNotAddedRanges(Range<Integer> range) {
        TreeRangeSet<Integer> rangeSet = TreeRangeSet.create();
        rangeSet.add(range);
        this.storedCodePoints.stream()
                .forEach(index -> rangeSet.remove(Range.singleton(index)));
        return rangeSet.asRanges().stream();
    }

    public void packCodePoints(IntStream points) {
        packCodePointRangesFinal(ImmutableList.of(ImmutableSet
                .copyOf(points.mapToObj(Integer::valueOf)::iterator)));
    }

    public void renderString(String str, Vector2f pos) {
        // Validate that we have all things to render.
        // If not, just load them now.
        ImmutableSet.Builder<Integer> codePointsToLoad = ImmutableSet.builder();
        str.codePoints().filter(x -> !this.storedCodePoints.get(x))
                .forEach(codePointsToLoad::add);
        packCodePointRanges(codePointsToLoad.build());
        int[] codePoints = str.codePoints().toArray();
        this.pixelsTexture.bind();
        FloatBuffer xpos = MemoryUtil.memAllocFloat(1);
        xpos.put(0, pos.getX());
        FloatBuffer ypos = MemoryUtil.memAllocFloat(1);
        ypos.put(0, pos.getY());
        STBTTAlignedQuad quad = STBTTAlignedQuad.calloc();
        try {
            for (int i = 0; i < codePoints.length; i++) {
                Integer char_index =
                        this.codePointToCharDataIndex.get(codePoints[i]);
                if (char_index == null) {
                    throw new DeallocatedException();
                }
                STBTruetype.stbtt_GetPackedQuad(this.charData, this.width,
                        this.height, char_index, xpos, ypos, quad, GL_TRUE);
                // TODO: optimize somehow?
                VBAO strRenderPos = Shapes.getQuad(getVertexData(quad));
                strRenderPos.tex = this.pixelsTexture;
                strRenderPos.draw();
                strRenderPos.destroy();
            }
        } finally {
            quad.free();
            MemoryUtil.memFree(xpos);
            MemoryUtil.memFree(ypos);
        }
    }

    private VertexData[] getVertexData(STBTTAlignedQuad quad) {
        VertexData[] data = new VertexData[4];
        data[0] = new VertexData().setXYZ(quad.x0(), quad.y0(), 0)
                .setUV(quad.s0(), quad.t0());
        data[1] = new VertexData().setXYZ(quad.x1(), quad.y0(), 0)
                .setUV(quad.s1(), quad.t0());
        data[2] = new VertexData().setXYZ(quad.x1(), quad.y1(), 0)
                .setUV(quad.s1(), quad.t1());
        data[3] = new VertexData().setXYZ(quad.x0(), quad.y1(), 0)
                .setUV(quad.s0(), quad.t1());
        return data;
    }

    private void destroy() {
        System.err.println("Destroying string renderer...");
        System.err.println("Whose fault?");
        Thread.dumpStack();
        this.charData.free();
        this.codePointToCharDataIndex.clear();
        this.storedCodePoints.clear();
        this.fontData.limit(0);
        STBTruetype.stbtt_PackEnd(this.context);
        this.pixels.limit(0);
    }
}
