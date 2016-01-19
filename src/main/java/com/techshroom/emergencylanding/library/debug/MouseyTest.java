package com.techshroom.emergencylanding.library.debug;

import com.flowpowered.math.vector.Vector2i;
import com.techshroom.emergencylanding.imported.Color;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.tex.ColorTexture;
import com.techshroom.emergencylanding.library.main.KMain;

public class MouseyTest extends KMain {

    private static DisplayLayer layer;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try {
            layer = DisplayLayer.initDisplay(0, 800, 500, "Testing MOUSE",
                    false, true, args);
            layer.getDisplayFPSTracker().enable(layer.getWindow());
            while (!layer.shouldClose()) {
                layer.loop(60);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (layer != null) {
                layer.destroy();
            }
            DisplayLayer.finalExitCall();
        }
    }

    private static boolean toggleNextChance;

    @Override
    public void onDisplayUpdate(int delta) {
        if (toggleNextChance) {
            toggleNextChance = false;
            layer.toggleFull();
        }
    }

    @Override
    public void init(DisplayLayer layer, String[] args) {
        layer.getMouseHelp().createFollowCursor(
                new ColorTexture(Color.RED, new Vector2i(10, 10)), 0, 0);
    }

}
