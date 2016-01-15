package emergencylanding.k.library.lwjgl.render;

import java.util.HashMap;

import emergencylanding.k.exst.mods.Mods;
import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.internalstate.Victor;
import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.internalstate.world.WorldManager;
import emergencylanding.k.library.lwjgl.Shapes;
import emergencylanding.k.library.main.KMain;
import emergencylanding.k.library.util.BoundingBox;
import emergencylanding.k.library.util.DrawableUtils;
import emergencylanding.k.library.util.LUtils;

public class RenderManager {

    private static HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender =
            new HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>>();

    public static void registerRenders() {
        HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> temp =
                new HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>>();
        Mods.registerRenders(temp);
        classToRender.putAll(temp);
        temp = new HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>>();
        KMain.getInst().registerRenders(temp);
        classToRender.putAll(temp);
        RenderManager.classToRender.put(ELEntity.class, new DefaultRender());
    }

    public static void doRender(World w, int delta) {
        for (ELEntity e : w.getEntityList()) {
            Victor p = e.getInterpolated();
            RenderManager.doRender(e, p);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void doRender(ELEntity e, Victor interpolated) {
        Render r = RenderManager.getRendererForClass(e.getClass());
        if (r == null) {
            throw new IllegalStateException("Missing default render!");
        }
        r.doRender(e, interpolated);
        if (LUtils.debugLevel >= 1) {
            BoundingBox bb = e.getBB();
            DrawableUtils.beginStandardEntityRender(e, interpolated.x,
                    interpolated.y, interpolated.z);
            VBAO bbbox = Shapes.getQuad(new VertexData().setRGB(255, 0, 0),
                    new VertexData().setXYZ((float) (bb.getWidth()),
                            (float) (bb.getHeight()), 0),
                    Shapes.XY);
            bbbox.draw().destroy();
            DrawableUtils.endStandardEntityRender();
        }
    }

    @SuppressWarnings("unchecked")
    public static Render<?> getRendererForClass(Class<? extends ELEntity> cls) {
        Render<?> r = null;
        if (RenderManager.classToRender.containsKey(cls)) {
            r = RenderManager.classToRender.get(cls);
        } else if (cls.getSuperclass() != null
                && ELEntity.class.isAssignableFrom(cls.getSuperclass())) {
            r = RenderManager.getRendererForClass(
                    (Class<? extends ELEntity>) cls.getSuperclass());
        }
        return r;
    }

    public static void render(int delta) {
        WorldManager.render(delta);
    }

}
