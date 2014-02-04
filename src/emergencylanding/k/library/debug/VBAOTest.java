package emergencylanding.k.library.debug;

import java.util.HashMap;

import org.lwjgl.opengl.Display;

import emergencylanding.k.imported.Sync.RunningAvg;
import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.internalstate.Victor;
import emergencylanding.k.library.lwjgl.DisplayLayer;
import emergencylanding.k.library.lwjgl.Shapes;
import emergencylanding.k.library.lwjgl.render.*;
import emergencylanding.k.library.lwjgl.tex.ColorTexture;
import emergencylanding.k.library.main.KMain;

public class VBAOTest extends KMain {
    VBAO quad = null;
    RunningAvg davg = new RunningAvg(10);

    int t = 0;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DisplayLayer.initDisplay(false, 1000, 600, "VBAO NewElTest", false,
                false, args);
        FPS.enable(0);
        while (!Display.isCloseRequested()) {
            DisplayLayer.loop(12000);
        }
        DisplayLayer.destroy();
    }

    @Override
    public void onDisplayUpdate(int delta) {
        quad.setXYZOff(new Victor(delta, delta, delta));
        quad.draw();
    }

    @Override
    public void init(String[] args) {
        float[] v1 = { 50, 200, 0, 1f, 1f, 1f };
        float[] v2 = { 50, 50, 0, 1f, 1f, 1f };
        float[] v3 = { 200, 50, 0, 1f, 1f, 1f };
        float[] v4 = { 200, 200, 0, 1f, 1f, 1f };
        int order = VertexData.COLOR_FIRST;
        VertexData[] verts = { new VertexData(order, v1),
                new VertexData(order, v2), new VertexData(order, v3),
                new VertexData(order, v4) };
        quad = Shapes.getQuad(verts);
        quad.setTexture(ColorTexture.BLUE);
    }

    @Override
    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
    }

}
