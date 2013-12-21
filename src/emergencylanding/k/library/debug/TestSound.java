package emergencylanding.k.library.debug;

import java.util.HashMap;

import org.lwjgl.opengl.Display;

import emergencylanding.k.library.internalstate.Entity;
import emergencylanding.k.library.lwjgl.DisplayLayer;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.main.KMain;
import emergencylanding.k.library.sound.SoundPlayer;
import emergencylanding.k.library.util.LUtils;

public class TestSound extends KMain {

    public static void main(String[] args) throws Exception {
	DisplayLayer.initDisplay(false, 800, 600, "Testing EL Sound", true,
		args);
	while (!Display.isCloseRequested()) {
	    DisplayLayer.loop(120);
	}
	DisplayLayer.destroy();
	System.exit(0);
    }

    private static boolean toggleNextChance;

    @Override
    public void onDisplayUpdate(int delta) {
	DisplayLayer.readDevices();
	if (toggleNextChance) {
	    toggleNextChance = false;
	    DisplayLayer.toggleFull();
	}
    }

    @Override
    public void init(String[] args) {
	SoundPlayer.playWAV(LUtils.TOP_LEVEL.getAbsolutePath()
		+ "\\res\\wav\\test.wav", 1.0f, 5.0f, true);
	SoundPlayer.playWAV(LUtils.TOP_LEVEL.getAbsolutePath()
		+ "\\res\\wav\\test.wav", 1.0f, 10f, true);
	SoundPlayer.playWAV(LUtils.TOP_LEVEL.getAbsolutePath()
		+ "\\res\\wav\\test.wav", 1.0f, 15f, true);
    }

    @Override
    public void registerRenders(
	    HashMap<Class<? extends Entity>, Render<? extends Entity>> classToRender) {

    }
}
