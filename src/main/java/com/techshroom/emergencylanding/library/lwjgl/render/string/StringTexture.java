package com.techshroom.emergencylanding.library.lwjgl.render.string;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_MAX_TEXTURE_SIZE;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexSubImage2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.OpenGLException;

import com.flowpowered.math.vector.Vector2d;
import com.techshroom.emergencylanding.library.lwjgl.render.GLData;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;
import com.techshroom.emergencylanding.library.shapeup.Rectangle;
import com.techshroom.emergencylanding.library.util.LUtils;

/**
 * Doesn't use {@link ELTexture} due to the nature of the constant sub image and
 * the single-channel buffer.
 * 
 * Usage of the texture will only work properly with the string rendering shader
 * that takes the red channel and maps it to all channels.
 */
public class StringTexture {

    private final ByteBuffer pixels;
    private final int width;
    private final int height;
    private transient int glTextureID;

    public StringTexture(ByteBuffer pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        ELTexture.addRunnableToQueue(this::bindOpenGLTextures);
    }

    private void bindOpenGLTextures() {
        this.glTextureID = glGenTextures();
        glActiveTexture(GL_TEXTURE0);

        try {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
                    GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            this.bind();
            GLData.notifyOnGLError("generatingBindings");
        } catch (OpenGLException ogle) {
            if (LUtils.debugLevel > 1) {
                System.err.println(
                        "OpenGL encountered an error while binding id #"
                                + this.glTextureID + ": "
                                + ogle.getLocalizedMessage());
            }
        }
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        GLData.notifyOnGLError("settingUnpackAlignment");
        System.err.println(
                "GL_MAX_TEXTURE_SIZE = " + glGetInteger(GL_MAX_TEXTURE_SIZE));
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0,
                GL_RED, GL_UNSIGNED_BYTE, this.pixels);
        GLData.notifyOnGLError("generatingInitialData");
        glGenerateMipmap(GL_TEXTURE_2D);
        GLData.notifyOnGLError("generatingMipmaps");
    }

    public int getWidth() {
        return this.width;
    }

    public ByteBuffer getPixels() {
        return this.pixels;
    }

    public int getHeight() {
        return this.height;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.glTextureID);
        GLData.notifyOnGLError("bindingStringTexture");
    }

    public void onUpdatedPixels(Vector2d offset, Rectangle changedPixels) {
        bind();
        glTexSubImage2D(GL_TEXTURE_2D, 0, (int) offset.getX(),
                (int) offset.getY(), (int) changedPixels.getWidth(),
                (int) changedPixels.getHeight(), GL_RED, GL_UNSIGNED_BYTE,
                this.pixels);
        GLData.notifyOnGLError("updatingStringTexture");
    }

}
