package emergencylanding.k.library.debug;

import java.util.HashMap;

import org.lwjgl.opengl.Display;

import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.lwjgl.DisplayLayer;
import emergencylanding.k.library.lwjgl.render.*;
import emergencylanding.k.library.main.KMain;

public class RotationTest extends KMain {
    private static boolean run = true;
    TestCollisionEntity tce, tce2;

    public static void main(String[] args) {
        try {
            DisplayLayer.initDisplay(false, 800, 500, "Testing Rotations",
                    false, false, args);
            while (run) {
                run = !Display.isCloseRequested();
                DisplayLayer.loop(120);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DisplayLayer.destroy();
            System.exit(0);
        }
    }

    @Override
    public void onDisplayUpdate(int delta) {
        tce.interpolate(delta);
        RenderManager.doRender(tce, tce.getInterpolated());
        tce2.interpolate(delta);
        RenderManager.doRender(tce2, tce2.getInterpolated());
    }

    @Override
    public void init(String[] args) {
        tce = new TestCollisionEntity(new World(), 200, 123, 0);
        tce.setPitch(0);
        tce2 = new TestCollisionEntity(new World(), 123, 123, 0);
        tce2.setPitch(45);
    }

    @Override
    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
        classToRender.put(TestCollisionEntity.class, new TextureRender());
    }

}
