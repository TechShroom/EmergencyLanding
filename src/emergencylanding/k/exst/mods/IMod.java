package emergencylanding.k.exst.mods;

import java.util.HashMap;

import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.main.KMain;

public interface IMod {

    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender);
    
    public void init(KMain instance);

}
