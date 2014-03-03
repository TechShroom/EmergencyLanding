package emergencylanding.k.library.debug;

import java.util.HashMap;

import org.lwjgl.opengl.Display;

import emergencylanding.k.imported.Sync;
import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.internalstate.EntityCollide;
import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.internalstate.world.WorldManager;
import emergencylanding.k.library.lwjgl.DisplayLayer;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.lwjgl.render.RenderManager;
import emergencylanding.k.library.lwjgl.render.TextureRender;
import emergencylanding.k.library.main.KMain;

public class CollisionsTest extends KMain {
    private static Thread is;
    private static boolean run = true;
    private static Thread ip;
    private static final int TICKS_PER_SECOND = 20;
    private static final int FRAMES_PER_SECOND = 1200;
    public static final int DISPLAY_FPS_INDEX = 0, IS_INDEX = FPS.genIndex(),
            INTERPOLATE_INDEX = FPS.genIndex();

    private static World w;
    private static EntityCollide e;
    private static EntityCollide e2;

    public static void main(String[] args) {
        try {
            DisplayLayer.initDisplay(false, 800, 500, "Testing Collisions",
                    false, false, args);
            FPS.enable(CollisionsTest.DISPLAY_FPS_INDEX);
            CollisionsTest.startISThreads();
            while (CollisionsTest.run) {
                CollisionsTest.run = !Display.isCloseRequested();
                DisplayLayer.loop(CollisionsTest.FRAMES_PER_SECOND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DisplayLayer.destroy();
            System.exit(0);
        }
    }

    private static void startISThreads() {
        Runnable isr = new Runnable() {
            Sync s = new Sync();

            @Override
            public void run() {
                FPS.init(CollisionsTest.IS_INDEX);
                while (CollisionsTest.run) {
                    e.setRelativeXYZ(0, 1, 0);
                    if (e.testCollide(e2)) {
                        System.err.println("**BOOM**");
                        run = false;
                    }
                    s.sync(CollisionsTest.TICKS_PER_SECOND);
                    int delta = FPS.update(CollisionsTest.IS_INDEX);
                    DisplayLayer.readDevices();
                    WorldManager.update(delta);
                }
            }
        };

        CollisionsTest.is = new Thread(isr);
        CollisionsTest.is.setName("Internal State Thread");
        CollisionsTest.is.start();

        Runnable ipr = new Runnable() {
            Sync s = new Sync();

            @Override
            public void run() {
                FPS.init(CollisionsTest.INTERPOLATE_INDEX);
                while (CollisionsTest.run) {
                    s.sync(CollisionsTest.FRAMES_PER_SECOND);
                    int delta = FPS.update(CollisionsTest.INTERPOLATE_INDEX);
                    WorldManager.interpolate(delta);
                }
            }
        };

        CollisionsTest.ip = new Thread(ipr);
        CollisionsTest.ip.setName("Interpolation Thread");
        CollisionsTest.ip.setDaemon(true);
        CollisionsTest.ip.start();
        System.err.println("ISThreads running!");
    }

    @Override
    public void onDisplayUpdate(int delta) {
        RenderManager.render(delta);
    }

    @Override
    public void init(String[] args) {
        w = new World();
        WorldManager.addWorldToSystem(w);
        e = new TestCollisionEntity(w, 50, 50, 50);
        e2 = new TestCollisionEntity(w, 50, 400, 50);
        e.setPitch(45);
        w.addEntity(e);
        w.addEntity(e2);
    }

    @Override
    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
            classToRender.put(EntityCollide.class, new TextureRender());
    }
}
