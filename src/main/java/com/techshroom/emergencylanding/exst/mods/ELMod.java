package com.techshroom.emergencylanding.exst.mods;

import java.util.HashMap;

import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.lwjgl.render.Render;

public abstract class ELMod implements IMod {

    @Override
    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
    }
}
