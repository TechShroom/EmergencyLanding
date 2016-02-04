package com.techshroom.emergencylanding.library.lwjgl.tex;

public interface Texture {

    /**
     * Binds the texture.
     */
    void bind();

    /**
     * Un-binds the texture.
     */
    void unbind();

    int getWidth();

    int getHeight();

}
