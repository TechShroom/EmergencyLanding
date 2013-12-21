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

	public static void PlayWAV(String soundFile) {
		try {
			iStream = LUtils.getInputStream(soundFile);
			bStream = new BufferedInputStream(iStream);
			wavA = AudioLoader.getAudio("WAV", bStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		wavA.playAsSoundEffect(1.0f, 1.0f, false);
		SoundStore.get().poll(0);
		if(Display.isCloseRequested()){
			AL.destroy();
		}
	}
}
