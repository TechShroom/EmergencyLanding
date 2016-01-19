package com.techshroom.emergencylanding.library.debug;

import com.techshroom.emergencylanding.imported.Color;
import com.techshroom.emergencylanding.imported.Sync.RunningAvg;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.lwjgl.render.VBAO;
import com.techshroom.emergencylanding.library.lwjgl.render.VertexData;
import com.techshroom.emergencylanding.library.lwjgl.tex.ColorTexture;
import com.techshroom.emergencylanding.library.main.KMain;
import com.techshroom.emergencylanding.library.util.DrawableUtils;

public class VBAOTest extends KMain {

    private static DisplayLayer layer;

    VBAO quad = null;
    RunningAvg davg = new RunningAvg(10);

    int t = 0;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try {
            layer = DisplayLayer.initDisplay(0, 1000, 600, "VBAO NewElTest",
                    false, false, args);
            layer.getDisplayFPSTracker().enable(layer.getWindow());
            while (!layer.shouldClose()) {
                layer.loop(120000);
            }
        } finally {
            if (layer != null) {
                layer.destroy();
            }
            DisplayLayer.finalExitCall();
        }
    }

    @Override
    public void onDisplayUpdate(int delta) {
        DrawableUtils.glBeginTrans(delta, delta, delta);
        this.quad.draw();
        DrawableUtils.glEndTrans();
    }

    @Override
    public void init(DisplayLayer layer, String[] args) {
        float[] v1 = { 50, 200, 0, 1f, 1f, 1f };
        float[] v2 = { 50, 50, 0, 1f, 1f, 1f };
        float[] v3 = { 200, 50, 0, 1f, 1f, 1f };
        float[] v4 = { 200, 200, 0, 1f, 1f, 1f };
        int order = VertexData.COLOR_FIRST;
        VertexData[] verts =
                { new VertexData(order, v1), new VertexData(order, v2),
                        new VertexData(order, v3), new VertexData(order, v4) };
        this.quad = Shapes.getQuad(verts);
        this.quad.setTexture(new ColorTexture(Color.BLUE));
        this.quad.setStatic(false);
    }

}
