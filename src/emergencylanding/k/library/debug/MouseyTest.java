package emergencylanding.k.library.debug;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.lwjgl.DisplayLayer;
import emergencylanding.k.library.lwjgl.control.Keys;
import emergencylanding.k.library.lwjgl.control.MouseHelp;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.lwjgl.tex.ColorTexture;
import emergencylanding.k.library.main.KMain;
import emergencylanding.k.library.util.DrawableUtils;
import emergencylanding.k.library.util.LUtils;

public class MouseyTest extends KMain implements KeyListener {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DisplayLayer.initDisplay(false, 1024, 640, "Testing " + LUtils.LIB_NAME,
                true, args);
        Keys.registerListener(new MouseyTest());
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
        BufferedImage colorRed = ColorTexture.RED.toBufferedImage();
        colorRed = DrawableUtils.scaledBufferedImage(colorRed, 10, 10);
        MouseHelp.createFollowCursor(colorRed, 0, 0);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.err.println(e);
        if (e.getKeyCode() == Keyboard.KEY_ESCAPE) {
            toggleNextChance = true;
        }
    }

    @Override
    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
        // TODO Auto-generated method stub

    }
}
