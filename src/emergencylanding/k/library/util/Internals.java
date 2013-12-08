package emergencylanding.k.library.util;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import k.core.util.Helper;

public class Internals {
	static GraphicsEnvironment ge = GraphicsEnvironment
			.getLocalGraphicsEnvironment();
	static GraphicsDevice gd = Internals.ge.getDefaultScreenDevice();

	private static Runtime r = Runtime.getRuntime();

	public static void exec(String string) {
		try {
			Internals.r.exec(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void say(String text) {
		Internals.exec("say " + text);
	}

	private static String[] mentions = {"You have no need to worry!",
			"Everything will be alright.", "This statement will not hurt you.",
			"Have fun!"};

	public static void mention(String text) {
		Internals.exec("say " + text + ". "
				+ Helper.Arrays.repeatRandomArray(Internals.mentions, 6)[0]);
	}

	public static void fullscreen(JFrame win) {
		if (win == null) {
			Internals.gd.setFullScreenWindow(null);
			return;
		}
		if (Internals.gd.isFullScreenSupported()) {
			win.setVisible(false);
			win.setUndecorated(true);
			Internals.gd.setFullScreenWindow(win);
		} else {
			win.setSize(10000, 10000);
		}
	}

	public static void disable() {
		Internals.gd.setFullScreenWindow(null);
	}

}
