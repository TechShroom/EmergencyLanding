package emergencylanding.k.library.debug;

import java.util.HashMap;

import org.lwjgl.opengl.Display;

import emergencylanding.k.imported.Sync;
import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.internalstate.GravEntity;
import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.internalstate.world.WorldManager;
import emergencylanding.k.library.lwjgl.DisplayLayer;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.lwjgl.render.RenderManager;
import emergencylanding.k.library.main.KMain;

public class SideTest_GraviteeeeeeeTest extends KMain {
    private static Thread is, ip;
    private static boolean run = true;
    private static final int TICKS_PER_SECOND = 60;
    private static final int FRAMES_PER_SECOND = 120;
    public static final int DISPLAY_FPS_INDEX = 0, IS_INDEX = FPS.genIndex(),
            INTERPOLATE_INDEX = FPS.genIndex();
    public ELEntity e;

    public static void main(String[] args) {
        try {
            DisplayLayer.initDisplay(false, 1152, 720, "Testing CrashCourse",
                    true, true, args);
            FPS.enable(SideTest_GraviteeeeeeeTest.DISPLAY_FPS_INDEX);
            SideTest_GraviteeeeeeeTest.startISThreads();
            while (SideTest_GraviteeeeeeeTest.run) {
                SideTest_GraviteeeeeeeTest.run = !Display.isCloseRequested();
                DisplayLayer.loop(SideTest_GraviteeeeeeeTest.FRAMES_PER_SECOND);
            }
            DisplayLayer.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startISThreads() {
        Runnable isr = new Runnable() {
            Sync s = new Sync();

            @Override
            public void run() {
                FPS.init(SideTest_GraviteeeeeeeTest.IS_INDEX);
                while (SideTest_GraviteeeeeeeTest.run) {
                    s.sync(SideTest_GraviteeeeeeeTest.TICKS_PER_SECOND);
                    int delta = FPS.update(SideTest_GraviteeeeeeeTest.IS_INDEX);
                    DisplayLayer.readDevices();
                    WorldManager.update(delta);
                }
            }
        };

        SideTest_GraviteeeeeeeTest.is = new Thread(isr);
        SideTest_GraviteeeeeeeTest.is.setName("Internal State Thread");
        SideTest_GraviteeeeeeeTest.is.start();

        Runnable ipr = new Runnable() {
            Sync s = new Sync();

            @Override
            public void run() {
                FPS.init(SideTest_GraviteeeeeeeTest.INTERPOLATE_INDEX);
                while (SideTest_GraviteeeeeeeTest.run) {
                    s.sync(SideTest_GraviteeeeeeeTest.FRAMES_PER_SECOND);
                    int delta = FPS
                            .update(SideTest_GraviteeeeeeeTest.INTERPOLATE_INDEX);
                    WorldManager.interpolate(delta);
                }
            }
        };

        SideTest_GraviteeeeeeeTest.ip = new Thread(ipr);
        SideTest_GraviteeeeeeeTest.ip.setName("Interpolation Thread");
        SideTest_GraviteeeeeeeTest.ip.setDaemon(true);
        SideTest_GraviteeeeeeeTest.ip.start();
        System.err.println("ISThreads running!");
    }

    @Override
    public void onDisplayUpdate(int delta) {
        float last = e.getInterpolated().lastY;
        float y = e.getInterpolated().y;
        if (y > last != ((int) e.getYVel()) > 0) {
            if (((int) e.getYVel()) == 0) {
            } else {
                System.err.println("vel != interp (velY=" + e.getYVel() + ")");
            }
        }
        RenderManager.render(delta);
    }

    @Override
    public void init(String[] args) {
        World w = new World();
        WorldManager.addWorldToSystem(w);
        e = new TestGravEntity(w);
        w.addEntity(e);
        for (int i = 0; i < 100; i++) {
            GravEntity ge = new TestGravEntity(w);
            ge.setXYZ(((float) Math.random() + 0.1f) * 1000f,
                    ((float) Math.random() + 2f) * 100f, 0f);
            w.addEntity(ge);
        }
    }

    @Override
    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
        // TODO Auto-generated method stub
    }
}