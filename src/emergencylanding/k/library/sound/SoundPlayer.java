package emergencylanding.k.library.sound;

import java.io.*;

import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.*;

import emergencylanding.k.library.util.LUtils;

public class SoundPlayer {
    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                AL.destroy();
            }
        });
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     */
    public static Audio playWAV(String soundFile) {
        return playWAV(soundFile, 1.0f);
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     * @param volume
     */
    public static Audio playWAV(String soundFile, float volume) {
        return playWAV(soundFile, volume, 1.0f);
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     * @param volume
     * @param pitch
     */
    public static Audio playWAV(String soundFile, float volume, float pitch) {
        return playWAV(soundFile, volume, pitch, false);
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     * @param volume
     * @param pitch
     * @param loop
     */
    public static Audio playWAV(String soundFile, float volume, float pitch,
            boolean loop) {
        Audio wavA = null;
        try {
            InputStream iStream = LUtils.getInputStream(soundFile);
            BufferedInputStream bStream = new BufferedInputStream(iStream);
            wavA = AudioLoader.getAudio("WAV", bStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SoundStore.get().setSoundVolume(volume);

        wavA.playAsSoundEffect(pitch, 1.0f, loop);
        return wavA;
    }
}
