package emergencylanding.k.library.internalstate.world;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import emergencylanding.k.library.lwjgl.render.RenderManager;

public class WorldManager {
	private static ArrayList<World> worlds = new ArrayList<World>();
	private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	private static Lock read = WorldManager.rwLock.readLock(),
			write = WorldManager.rwLock.writeLock();

	public static void addWorldToSystem(World w) {
		WorldManager.write.lock();
		WorldManager.worlds.add(w);
		WorldManager.write.unlock();
	}

	public static void render(int delta) {
		WorldManager.read.lock();
		for (World w : WorldManager.worlds) {
			RenderManager.doRender(w, delta);
		}
		WorldManager.read.unlock();
	}

	public static void update(float delta) {
		WorldManager.read.lock();
		for (World w : WorldManager.worlds) {
			w.update(delta);
		}
		WorldManager.read.unlock();
	}

	public static void interpolate(int delta) {
		WorldManager.read.lock();
		for (World w : WorldManager.worlds) {
			w.interpolate(delta);
		}
		WorldManager.read.unlock();
	}
}
