package com.techshroom.emergencylanding.library.debug;

import java.util.HashMap;

import com.techshroom.emergencylanding.imported.Sync;
import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.internalstate.GravEntity;
import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.internalstate.world.WorldManager;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.render.Render;
import com.techshroom.emergencylanding.library.lwjgl.render.RenderManager;
import com.techshroom.emergencylanding.library.main.KMain;

public class SideTest_GraviteeeeeeeTest extends KMain {

    private static DisplayLayer layer;
    private static Thread is, ip;
    private static boolean run = true;
    private static final int TICKS_PER_SECOND = 60;
    private static final int FRAMES_PER_SECOND = 120;
    public ELEntity e;

    public static void main(String[] args) {
        try {
            layer = DisplayLayer.initDisplay(0, 1152, 720,
                    "Testing CrashCourse", true, true, args);
            layer.getDisplayFPSTracker().enable(layer.getWindow());
            SideTest_GraviteeeeeeeTest.startISThreads();
            while (SideTest_GraviteeeeeeeTest.run) {
                SideTest_GraviteeeeeeeTest.run = !layer.shouldClose();
                layer.loop(FRAMES_PER_SECOND);
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

    private static void startISThreads() {
        Runnable isr = new Runnable() {

            Sync s = new Sync();

            @Override
            public void run() {
                FPS fps = new FPS("is");
                fps.init();
                while (SideTest_GraviteeeeeeeTest.run) {
                    this.s.sync(SideTest_GraviteeeeeeeTest.TICKS_PER_SECOND);
                    int delta = fps.update();
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
                FPS fps = new FPS("ip");
                fps.init();
                while (SideTest_GraviteeeeeeeTest.run) {
                    this.s.sync(SideTest_GraviteeeeeeeTest.FRAMES_PER_SECOND);
                    int delta = fps.update();
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
        float last = this.e.getInterpolated().lastY;
        float y = this.e.getInterpolated().y;
        if (y > last != ((int) this.e.getYVel()) > 0) {
            if (((int) this.e.getYVel()) != 0) {
                System.err.println(
                        "vel != interp (velY=" + this.e.getYVel() + ")");
            }
        }
        RenderManager.render(delta);
    }

    @Override
    public void init(DisplayLayer layer, String[] args) {
        World w = new World();
        WorldManager.addWorldToSystem(w);
        this.e = new TestGravEntity(w);
        w.addEntity(this.e);
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