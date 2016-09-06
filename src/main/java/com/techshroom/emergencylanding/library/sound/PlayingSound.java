package com.techshroom.emergencylanding.library.sound;

import org.lwjgl.openal.AL10;

public class PlayingSound {

    private final int source;

    public PlayingSound(int source) {
        this.source = source;
    }

    public int getSource() {
        return this.source;
    }

    public void stop() {
        AL10.alSourceStop(this.source);
    }

}
