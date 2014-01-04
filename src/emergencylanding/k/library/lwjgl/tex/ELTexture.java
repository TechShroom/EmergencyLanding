package emergencylanding.k.library.lwjgl.tex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.MipMap;

import emergencylanding.k.library.debug.Memory;
import emergencylanding.k.library.exceptions.lwjgl.TextureBindException0;
import emergencylanding.k.library.lwjgl.render.GLData;
import emergencylanding.k.library.main.KMain;
import emergencylanding.k.library.util.LUtils;

public abstract class ELTexture {
    public static class DestTexture extends ELTexture {

        public DestTexture(ELTexture texture) {
            buf = ByteBuffer.allocateDirect(1);
            dim = new Dimension(1, 1);
            setConstructingOverrideId(texture.id);
            init();
            ELTexture.texlist.remove(getID());
            ELTexture.currentSpace -= buf.capacity();
            ELTexture.removedIDs.add(getID());
            GL11.glDeleteTextures(getID());
            texture.destruction0();
        }

        @Override
        public void setup() {

        }

        @Override
        protected BufferedImage toBufferedImageAbstract() {
            throw new UnsupportedOperationException("This texture is removed");
        }

        @Override
        protected void onDestruction() {
        }

    }

    private int id = -1;
    private static int useID = -1;
    static {
        System.gc();
    }
    public static final long TOTAL_TEXTURE_SPACE = Runtime.getRuntime()
            .maxMemory() / 2;
    // private static IntBuffer ids = BufferUtils.createIntBuffer(1);
    private static HashMap<Integer, ELTexture> texlist = new HashMap<Integer, ELTexture>();
    private static ArrayList<Integer> removedIDs = new ArrayList<Integer>();
    public static long currentSpace = 0;
    protected static boolean mipmapsEnabled = true;
    public ByteBuffer buf = null;
    public Dimension dim = null;
    private static ArrayList<Runnable> glThreadQueue = new ArrayList<Runnable>();
    static {
        System.gc();
        Memory.printAll();
        Field f;
        try {
            f = ContextCapabilities.class.getDeclaredField("glGenerateMipmap");
            f.setAccessible(true);
            ELTexture.mipmapsEnabled = f.getLong(GLContext.getCapabilities()) != 0;
        } catch (Exception e) {
            ELTexture.mipmapsEnabled = false;
        }
        LUtils.print("3.0 mipmaps? " + ELTexture.mipmapsEnabled);
    }

    // Define static Textures AFTER this comment

    public static final ELTexture invisible = new ColorTexture(new Color(0, 0,
            0, 0), new Dimension(1, 1));

    public ELTexture() {
    }

    private void bind0() {
        final ELTexture texObj = this;
        Runnable runnable = null;
        synchronized (glThreadQueue) {
            glThreadQueue.add(runnable = new Runnable() {
                @Override
                public void run() {
                    if (buf == null || dim == null) {
                        throw new TextureBindException0(
                                "A required variable is null when creating textures!");
                    } else if (buf.capacity() < dim.height * dim.width * 4) {
                        ByteBuffer tmp = ByteBuffer.allocateDirect(dim.height
                                * dim.width * 4);
                        tmp.put(buf);
                        buf = tmp;
                        buf.rewind();
                    }
                    buf.rewind();
                    ELTexture lookAlike;
                    if ((lookAlike = ELTexture.similar(texObj)) != null) {
                        id = lookAlike.id;
                        LUtils.print("Overrode id: " + id);
                        ELTexture.texlist.put(lookAlike.id, texObj);
                    } else {
                        boolean override = false;
                        ELTexture.currentSpace += buf.capacity();
                        if (ELTexture.removedIDs.size() > 0
                                && ELTexture.useID == -1) {
                            id = ELTexture.removedIDs.get(0);
                            ELTexture.removedIDs.remove(0);
                        }
                        if (ELTexture.useID > -1) {
                            LUtils.print("Force-overrode id: " + id);
                            override = true;
                            id = ELTexture.useID;
                            ELTexture.currentSpace -= ELTexture.texlist.get(id).buf
                                    .capacity();
                        }
                        if (ELTexture.currentSpace < ELTexture.TOTAL_TEXTURE_SPACE
                                && id == -1 && ELTexture.useID == -1) {
                            id = GL11.glGenTextures();
                        } else {
                            LUtils.print("WARNING! Texture limit reached, "
                                    + "not adding new textures!");
                            return;
                        }
                        // Create a new texture object in memory and bind it
                        GL13.glActiveTexture(GL13.GL_TEXTURE0);
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

                        // All RGB bytes are aligned to each other and each
                        // component is 1
                        // byte
                        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                        if (!ELTexture.mipmapsEnabled) {
                            GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                                    GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
                            GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                                    GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
                            GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                                    GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                            GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                                    GL11.GL_TEXTURE_MIN_FILTER,
                                    GL11.GL_LINEAR_MIPMAP_LINEAR);
                            GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                                    GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
                        }
                        // Upload the texture data and generate mip maps (for
                        // scaling)
                        // ByteBuffer tmp = buf.duplicate();
                        // buf.rewind();
                        if (override) {
                            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0,
                                    dim.width, dim.height, GL11.GL_RGBA,
                                    GL11.GL_UNSIGNED_BYTE, buf);
                        } else {
                            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0,
                                    GL11.GL_RGBA, dim.width, dim.height, 0,
                                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
                        }
                        // buf = tmp;
                        if (ELTexture.mipmapsEnabled) {
                            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
                        } else {
                            int err = MipMap.gluBuild2DMipmaps(
                                    GL11.GL_TEXTURE_2D, id, dim.width,
                                    dim.height, GL11.GL_RGBA,
                                    GL11.GL_UNSIGNED_BYTE, buf);
                            if (err != 0) {
                                LUtils.print("error while running build2dMipMaps: "
                                        + err);
                            }
                        }
                        ELTexture.texlist.put(id, texObj);
                    }
                }
            });
        }
        if (Thread.currentThread() == KMain.getDisplayThread()) {
            runnable.run();
            glThreadQueue.remove(runnable);
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
        synchronized (glThreadQueue) {
            for (Runnable r : glThreadQueue) {
                r.run();
            }
            glThreadQueue.clear();
        }
    }

    public boolean isLookAlike(ELTexture t) {
        return t.buf.equals(buf) && t.dim.equals(dim);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ELTexture && isLookAlike((ELTexture) o);
    }

    public abstract void setup();

    public int getID() {
        return id;
    }

    public void bind() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getID());
    }

    public void unbind() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, GLData.NONE);
    }

    public void glTextureVertex(float s, float t) {
        GL11.glTexCoord2f(s, t);
    }

    public int getWidth() {
        return dim.width;
    }

    public int getHeight() {
        return dim.height;
    }

    protected void init() {
        setup();
        bind0();
    }

    protected void setConstructingOverrideId(int id) {
        ELTexture.useID = id;
    }

    public BufferedImage toBufferedImage() {
        try {
            return toBufferedImageAbstract();
        } catch (Exception e) {
            new IllegalStateException(
                    "Didn't expect an error, running on backup", e)
                    .printStackTrace();
            return toBufferedImageBackup();
        }
    }

    protected abstract BufferedImage toBufferedImageAbstract();

    private BufferedImage toBufferedImageBackup() {
        int width = dim.width;
        int height = dim.height;
        int[] packedPixels = new int[width * height * 4];
        buf.rewind();
        // DisplayLayer.print("id " + id + " buf capacity " + buf.capacity());
        int bufferInd = 0;
        for (int row = height - 1; row >= 0; row--) {
            for (int col = 0; col < width; col++) {
                int R, G, B, A;
                R = buf.get(bufferInd++);
                G = buf.get(bufferInd++);
                B = buf.get(bufferInd++);
                A = buf.get(bufferInd++) & 0xff;
                int index = row * width + col;
                // DisplayLayer.print("recv " + R + "-" + G + "-" + B + "-" +
                // A);
                packedPixels[index] = B + (G << 8) + (R << 16) + (A << 24);
            }
        }
        BufferedImage img = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        int bitMask[] = new int[] { 0xff0000, 0xff00, 0xff, 0xff000000 };
        SinglePixelPackedSampleModel sampleModel = new SinglePixelPackedSampleModel(
                DataBuffer.TYPE_INT, width, height, bitMask);
        WritableRaster wr = Raster.createWritableRaster(sampleModel, null);
        wr.setPixels(0, 0, width, height, packedPixels);
        img.setData(wr);
        return img;
    }

    public void kill() {
        new DestTexture(this);
        System.gc();
    }

    private void destruction0() {
        unbind();
        onDestruction();
    }

    protected abstract void onDestruction();

}
