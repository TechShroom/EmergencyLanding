package emergencylanding.k.library.debug;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.Display;

import emergencylanding.k.imported.Sync;
import emergencylanding.k.library.internalstate.Entity;
import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.internalstate.world.WorldManager;
import emergencylanding.k.library.lwjgl.DisplayLayer;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.lwjgl.render.RenderManager;
import emergencylanding.k.library.main.KMain;

public class WorldAndEntityTest extends KMain {
	private static Thread is, ip;
	private static boolean run = true;
	private static final int TICKS_PER_SECOND = 20;
	private static final int FRAMES_PER_SECOND = 120;
	public static final int DISPLAY_FPS_INDEX = 0, IS_INDEX = FPS.genIndex(),
			INTERPOLATE_INDEX = FPS.genIndex();

	public static void main(String[] args) {
		try {
			DisplayLayer.initDisplay(false, 800, 500, "Testing CrashCourse",
					true, true, args);
			FPS.enable(WorldAndEntityTest.DISPLAY_FPS_INDEX);
			WorldAndEntityTest.startISThreads();
			while (WorldAndEntityTest.run) {
				WorldAndEntityTest.run = !Display.isCloseRequested();
				DisplayLayer.loop(WorldAndEntityTest.FRAMES_PER_SECOND);
			}
			DisplayLayer.destroy();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void startISThreads() {
		Runnable isr = new Runnable() {
			Sync s = new Sync();

			@Override
			public void run() {
				FPS.init(WorldAndEntityTest.IS_INDEX);
				while (WorldAndEntityTest.run) {
					s.sync(WorldAndEntityTest.TICKS_PER_SECOND);
					int delta = FPS.update(WorldAndEntityTest.IS_INDEX);
					DisplayLayer.readDevices();
					WorldManager.update(delta);
				}
			}
		};

		WorldAndEntityTest.is = new Thread(isr);
		WorldAndEntityTest.is.setName("Internal State Thread");
		WorldAndEntityTest.is.start();

		Runnable ipr = new Runnable() {
			Sync s = new Sync();

			@Override
			public void run() {
				FPS.init(WorldAndEntityTest.INTERPOLATE_INDEX);
				while (WorldAndEntityTest.run) {
					s.sync(WorldAndEntityTest.FRAMES_PER_SECOND);
					int delta = FPS
							.update(WorldAndEntityTest.INTERPOLATE_INDEX);
					WorldManager.interpolate(delta);
				}
			}
		};

		WorldAndEntityTest.ip = new Thread(ipr);
		WorldAndEntityTest.ip.setName("Interpolation Thread");
		WorldAndEntityTest.ip.setDaemon(true);
		WorldAndEntityTest.ip.start();
		System.err.println("ISThreads running!");
	}

	@Override
	public void onDisplayUpdate(int delta) {
		RenderManager.render(delta);
	}

	@Override
	public void init(String[] args) {
		World w = new World();
		WorldManager.addWorldToSystem(w);
		for (int i = 0; i < 500; i++) {
			Entity e = new TestEntity();
			w.addEntity(e);
			e.setXYZ(0, 0, 0);
			e.setXYZVel(1, 1, 0);
		}
	}

	@Override
	public void registerRenders(
			HashMap<Class<? extends Entity>, Render<? extends Entity>> classToRender) {
		// TODO Auto-generated method stub

	}
}
