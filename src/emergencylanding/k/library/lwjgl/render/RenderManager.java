package emergencylanding.k.library.lwjgl.render;

import java.util.HashMap;

import emergencylanding.k.exst.mods.Mods;
import emergencylanding.k.library.internalstate.Entity;
import emergencylanding.k.library.internalstate.Victor;
import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.internalstate.world.WorldManager;
import emergencylanding.k.library.main.KMain;

public class RenderManager {
	private static HashMap<Class<? extends Entity>, Render<? extends Entity>> classToRender = new HashMap<Class<? extends Entity>, Render<? extends Entity>>();

	public static void registerRenders() {
		Mods.registerRenders(RenderManager.classToRender);
		RenderManager.classToRender.put(Entity.class, new DefaultRender());
		KMain.getInst().registerRenders(RenderManager.classToRender);
	}

	public static void doRender(World w, int delta) {
		for (Entity e : w.getEntityList()) {
			Victor p = e.getInterpolated();
			RenderManager.doRender(e, p);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void doRender(Entity e, Victor interpolated) {
		Render r = RenderManager.getRendererForClass(e.getClass());
		if (r == null) {
			throw new IllegalStateException("Missing default render!");
		}
		r.doRender(e, interpolated);
	}

	@SuppressWarnings("unchecked")
	public static Render<?> getRendererForClass(Class<? extends Entity> cls) {
		Render<?> r = null;
		if (RenderManager.classToRender.containsKey(cls)) {
			r = RenderManager.classToRender.get(cls);
		} else if (cls.getSuperclass() != null
				&& Entity.class.isAssignableFrom(cls.getSuperclass())) {
			r = RenderManager.getRendererForClass((Class<? extends Entity>) cls
					.getSuperclass());
		}
		return r;
	}

	public static void render(int delta) {
		WorldManager.render(delta);
	}

}
