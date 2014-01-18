package emergencylanding.k.library.debug;

import java.util.HashMap;

import org.lwjgl.opengl.Display;

import emergencylanding.k.library.internalstate.ELEntity;
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

    @Override
    public void onDisplayUpdate(int delta) {
        DisplayLayer.readDevices();
    }

    @Override
    public void init(String[] args) {
        SoundPlayer.playWAV(LUtils.getELTop() + "\\res\\wav\\test.wav", 1.0f,
                .50f, true);
        SoundPlayer.playWAV(LUtils.getELTop() + "\\res\\wav\\test.wav", 1.0f,
                .10f, true);
        SoundPlayer.playWAV(LUtils.getELTop() + "\\res\\wav\\test.wav", 1.0f,
                2.15f, true);
    }

    @Override
    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {

    }
}
