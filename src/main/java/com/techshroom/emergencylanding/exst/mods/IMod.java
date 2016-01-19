package com.techshroom.emergencylanding.exst.mods;

import java.util.HashMap;

import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.lwjgl.render.Render;
import com.techshroom.emergencylanding.library.main.KMain;

public interface IMod {

    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender);

    public void init(KMain instance);

}
