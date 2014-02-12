package emergencylanding.k.library.main;

import java.util.ArrayList;
import java.util.HashMap;

import k.core.util.classes.StackTraceInfo;
import emergencylanding.k.exst.mods.IMod;
import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.util.LUtils;

public abstract class KMain {
    private static KMain insts = null;
    private static Thread displayThread = null;

    public abstract void onDisplayUpdate(int delta);

    public abstract void init(String[] args);

    public static void setInst(KMain inst) {
        try {
            LUtils.checkAccessor("emergencylanding.k.library.*",
                    StackTraceInfo.getInvokingClassName());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        KMain.insts = inst;
    }

    public static KMain getInst() {
        return KMain.insts;
    }

    public static void setDisplayThread(Thread t) {
        try {
            LUtils.checkAccessor("emergencylanding.k.library.*",
                    StackTraceInfo.getInvokingClassName());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
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
