package com.techshroom.emergencylanding.library.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.techshroom.emergencylanding.exst.mods.IMod;
import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.render.Render;
import com.techshroom.emergencylanding.library.util.LUtils;

public abstract class KMain {

    private static KMain insts = null;
    private static Thread displayThread = null;

    public abstract void onDisplayUpdate(int delta);

    public abstract void init(DisplayLayer layer, String[] args);

    public static void setInst(KMain inst) {
        KMain.insts = inst;
    }

    public static KMain getInst() {
        return KMain.insts;
    }

    public static void setDisplayThread(Thread t) {
        KMain.displayThread = t;
        LUtils.print("Display thread set");
    }

    public static Thread getDisplayThread() {
        return KMain.displayThread;
    }

    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
    }

    public void loadMods(ArrayList<IMod> mods) {

    }
}
