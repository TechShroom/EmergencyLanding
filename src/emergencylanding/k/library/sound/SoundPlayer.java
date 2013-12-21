package emergencylanding.k.library.sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;

import emergencylanding.k.library.util.LUtils;

public class SoundPlayer {

    private static Audio wavA;
    private static InputStream iStream;
    private static BufferedInputStream bStream;

    /**
     * Plays .wav file
     * 
     * @param soundFile
     */
    public static void playWAV(String soundFile) {
	try {
	    iStream = LUtils.getInputStream(soundFile);
	    bStream = new BufferedInputStream(iStream);
	    wavA = AudioLoader.getAudio("WAV", bStream);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	wavA.playAsSoundEffect(1.0f, 1.0f, false);
	SoundStore.get().poll(0);
	if (Display.isCloseRequested()) {
	    AL.destroy();
	}
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     * @param volume
     */
    public static void playWAV(String soundFile, float volume) {
	try {
	    iStream = LUtils.getInputStream(soundFile);
	    bStream = new BufferedInputStream(iStream);
	    wavA = AudioLoader.getAudio("WAV", bStream);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	SoundStore.get().setSoundVolume(volume);

	wavA.playAsSoundEffect(1.0f, 1.0f, false);
	if (Display.isCloseRequested()) {
	    AL.destroy();
	}
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     * @param volume
     * @param pitch
     */
    public static void playWAV(String soundFile, float volume, float pitch) {
	try {
	    iStream = LUtils.getInputStream(soundFile);
	    bStream = new BufferedInputStream(iStream);
	    wavA = AudioLoader.getAudio("WAV", bStream);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	SoundStore.get().setSoundVolume(volume);

	wavA.playAsSoundEffect(pitch, 1.0f, false);
	if (Display.isCloseRequested()) {
	    AL.destroy();
	}
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     * @param volume
     * @param pitch
     */
    public static void playWAV(String soundFile, float volume, float pitch,
	    boolean loop) {
	try {
	    iStream = LUtils.getInputStream(soundFile);
	    bStream = new BufferedInputStream(iStream);
	    wavA = AudioLoader.getAudio("WAV", bStream);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	SoundStore.get().setSoundVolume(volume);

	wavA.playAsSoundEffect(pitch, 1.0f, loop);
	if (Display.isCloseRequested()) {
	    AL.destroy();
	}
    }
}
