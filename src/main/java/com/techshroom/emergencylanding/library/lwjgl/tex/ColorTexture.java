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
