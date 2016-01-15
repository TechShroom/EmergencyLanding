package emergencylanding.k.exst.mods;

import java.util.HashMap;

import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.lwjgl.render.Render;

public abstract class ELMod implements IMod {

    @Override
    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
    }
}
