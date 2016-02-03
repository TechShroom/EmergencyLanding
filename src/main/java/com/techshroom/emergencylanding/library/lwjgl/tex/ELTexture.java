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

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexSubImage2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.OpenGLException;

import com.flowpowered.math.vector.Vector2i;
import com.techshroom.emergencylanding.imported.Color;
import com.techshroom.emergencylanding.library.debug.Memory;
import com.techshroom.emergencylanding.library.exceptions.lwjgl.RuntimeTextureBindException;
import com.techshroom.emergencylanding.library.lwjgl.render.GLData;
import com.techshroom.emergencylanding.library.main.KMain;
import com.techshroom.emergencylanding.library.util.LUtils;

public abstract class ELTexture {

    public static class DestTexture extends ELTexture {

        public static HashMap<Integer, Integer> removed =
                new HashMap<Integer, Integer>();

        public DestTexture(ELTexture texture) {
            this.buf = ByteBuffer.allocateDirect(1);
            this.dim = new Vector2i(1, 1);
            setConstructingOverrideId(texture.id);
            init();
            ELTexture.texlist.remove(getID());
            ELTexture.currentSpace -= this.buf.capacity();
            glDeleteTextures(getID());
            removed.put(getID(), getID());
            texture.destruction0();
        }

        @Override
        public void setup() {
        }

        @Override
        protected void onDestruction() {
        }

    }

    private int id = -1;
    private int useID = -1;
    // times two-thirds, but overflow conscious
    public static final long TOTAL_TEXTURE_SPACE =
            (Runtime.getRuntime().maxMemory() / 3) * 2;
    // private static IntBuffer ids = BufferUtils.createIntBuffer(1);
    private static Map<Integer, ELTexture> texlist =
            new HashMap<Integer, ELTexture>();
    public static long currentSpace = 0;
    public ByteBuffer buf = null;
    public Vector2i dim = null;
    private static ArrayList<Runnable> glThreadQueue =
            new ArrayList<Runnable>();
    private static ArrayList<Runnable> queueLater = new ArrayList<Runnable>();
    /**
     * Maximum added runnables while runnables are being processed, to avoid
     * freezing threads.
     */
    private static final int MAX_ADDED_BINDINGS = 2048;
    private static volatile AtomicBoolean binding = new AtomicBoolean(false);
    static {
        System.gc();
        Memory.printAll();
    }

    // Define static Textures AFTER this comment

    public static final ELTexture invisible =
            new ColorTexture(new Color(0, 0, 0, 0));

    public ELTexture() {
    }

    private void bind0() {
        if (KMain.getDisplayThread() == null) {
            throw new NullPointerException("Display Thread not init yet!");
        }
        final ELTexture texObj = this;
        Runnable runnable = null;
        synchronized (glThreadQueue) {
            glThreadQueue.add(runnable = new Runnable() {

                @Override
                public void run() {
                    if (ELTexture.this.buf == null
                            || ELTexture.this.dim == null) {
                        throw new RuntimeTextureBindException(
                                "A required variable is null when creating textures!");
                    } else if (ELTexture.this.buf
                            .capacity() < ELTexture.this.dim.getY()
                                    * ELTexture.this.dim.getX() * 4) {
                        ByteBuffer tmp = ByteBuffer
                                .allocateDirect(ELTexture.this.dim.getY()
                                        * ELTexture.this.dim.getX() * 4);
                        tmp.put(ELTexture.this.buf);
                        ELTexture.this.buf = tmp;
                        ELTexture.this.buf.rewind();
                    }
                    ELTexture.this.buf.rewind();
                    ELTexture lookAlike;
                    if ((lookAlike = ELTexture.similar(texObj)) != null) {
                        ELTexture.this.id = lookAlike.id;
                        if (LUtils.debugLevel > 1) {
                            LUtils.print("Overrode id: " + ELTexture.this.id
                                    + " (obj=" + texObj + ", overridden="
                                    + lookAlike + ")");
                        }
                        ELTexture.texlist.put(lookAlike.id, texObj);
                    } else {
                        boolean override = false;
                        ELTexture.currentSpace += ELTexture.this.buf.capacity();
                        if (ELTexture.this.useID > -1) {
                            override = true;
                            ELTexture.this.id = ELTexture.this.useID;
                            if (LUtils.debugLevel > 1) {
                                LUtils.print("Force-overrode id: "
                                        + ELTexture.this.id);
                            }
                            if (ELTexture.texlist
                                    .get(ELTexture.this.id) != null) {
                                ELTexture.currentSpace -= ELTexture.texlist
                                        .get(ELTexture.this.id).buf.capacity();
                            } else {
                                LUtils.print("Interesting, it appears that id "
                                        + ELTexture.this.id
                                        + " is null. This shouldn't be happening, but we'll let it slide for now.");
                            }
                        }
                        if (ELTexture.currentSpace < ELTexture.TOTAL_TEXTURE_SPACE
                                && ELTexture.this.id == -1
                                && ELTexture.this.useID == -1) {
                            ELTexture.this.id = glGenTextures();
                        } else if (ELTexture.this.id == -1
                                && ELTexture.this.useID == -1) {
                            LUtils.print("WARNING! Texture limit reached, "
                                    + "not adding new textures! ("
                                    + TOTAL_TEXTURE_SPACE + " < " + currentSpace
                                    + ")");
                            return;
                        }
                        if (DestTexture.removed
                                .containsKey(ELTexture.this.id)) {
                            DestTexture.removed.remove(ELTexture.this.id);
                        }
                        // Create a new texture object in memory and bind it
                        glActiveTexture(GL_TEXTURE0);
                        try {
                            glTexParameteri(GL_TEXTURE_2D,
                                    GL_TEXTURE_MIN_FILTER,
                                    GL_LINEAR_MIPMAP_LINEAR);
                            glTexParameteri(GL_TEXTURE_2D,
                                    GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                            glBindTexture(GL_TEXTURE_2D, ELTexture.this.id);
                        } catch (OpenGLException ogle) {
                            if (LUtils.debugLevel > 1) {
                                System.err.println(
                                        "OpenGL encountered an error while binding id #"
                                                + ELTexture.this.id + ": "
                                                + ogle.getLocalizedMessage());
                            }
                        }

                        // All RGB bytes are aligned to each other and each
                        // component is 1
                        // byte
                        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
                        // Upload the texture data and generate mip maps (for
                        // scaling)
                        if (override) {
                            glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0,
                                    ELTexture.this.dim.getX(),
                                    ELTexture.this.dim.getY(), GL_RGBA,
                                    GL_UNSIGNED_BYTE, ELTexture.this.buf);
                        } else {
                            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                                    ELTexture.this.dim.getX(),
                                    ELTexture.this.dim.getY(), 0, GL_RGBA,
                                    GL_UNSIGNED_BYTE, ELTexture.this.buf);
                        }
                        glGenerateMipmap(GL_TEXTURE_2D);
                        ELTexture.texlist.put(ELTexture.this.id, texObj);
                    }
                }
            });
        }
        if (Thread.currentThread() == KMain.getDisplayThread()) {
            runnable.run();
            glThreadQueue.remove(runnable);
            return;
        }
        while (glThreadQueue.contains(runnable)) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
    }

    private static ELTexture similar(ELTexture texture) {
        ELTexture t1 = null;
        for (ELTexture t : ELTexture.texlist.values()) {
            if (t.isLookAlike(texture)) {
                t1 = t;
                break;
            }
        }
        return t1;
    }

    public static void doBindings() {
        int added = 0;
        while (true) {
            // keep processing and reloading until no more are here. This could
            // potentially cause infinite loops, so be careful!
            synchronized (glThreadQueue) {
                binding.set(true);
                for (Runnable r : glThreadQueue) {
                    r.run();
                    GLData.notifyOnGLError("runningBindings");
                }
                glThreadQueue.clear();
            }
            binding.set(false);
            synchronized (queueLater) {
                if (queueLater.size() == 0) {
                    return;
                }
                synchronized (glThreadQueue) {
                    added += queueLater.size();
                    if (added > MAX_ADDED_BINDINGS) {
                        for (int i = 0; i < 100 && i < reported.size(); i++) {
                            reported.get(i).printStackTrace();
                        }
                        reported.clear();
                        throw new RuntimeException("Too many bindings! ("
                                + added + " > " + MAX_ADDED_BINDINGS + ")");
                    }
                    glThreadQueue.addAll(queueLater);
                    queueLater.clear();
                }
            }
        }
    }

    private static List<Throwable> reported = new ArrayList<Throwable>();

    public static void addRunnableToQueue(Runnable r) {
        if (Thread.currentThread() == KMain.getDisplayThread()) {
            r.run();
            return;
        }
        if (binding.get()) {
            reported.add(new Throwable("tracert"));
            synchronized (queueLater) {
                queueLater.add(r);
            }
            return;
        }
        synchronized (glThreadQueue) {
            glThreadQueue.add(r);
        }
    }

    public boolean isLookAlike(ELTexture t) {
        return t.buf.equals(this.buf) && t.dim.equals(this.dim);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ELTexture && isLookAlike((ELTexture) o);
    }

    public abstract void setup();

    public int getID() {
        return this.id;
    }

    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        try {
            glBindTexture(GL_TEXTURE_2D, getID());
        } catch (OpenGLException ogle) {
            System.err.println("OpenGL encountered an error while binding id #"
                    + this.id + ": " + ogle.getLocalizedMessage());
        }
    }

    public void unbind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, GLData.NONE);
    }

    public int getWidth() {
        return this.dim.getX();
    }

    public int getHeight() {
        return this.dim.getY();
    }

    protected void init() {
        setup();
        bind0();
    }

    protected void setConstructingOverrideId(int id) {
        this.useID = id;
    }

    public void kill() {
        if (DestTexture.removed.containsKey(this.id)) {
            return;
        }
        new DestTexture(this);
        System.gc();
    }

    public GLFWImage convertToGLFWImage() {
        return GLFWImage.create().set(this.dim.getX(), this.dim.getY(),
                this.buf);
    }

    private void destruction0() {
        unbind();
        onDestruction();
    }

    protected abstract void onDestruction();

}
