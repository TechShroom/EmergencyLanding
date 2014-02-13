package emergencylanding.k.exst.mods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import k.core.util.classes.ClassPathHack;
import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.main.KMain;
import emergencylanding.k.library.util.LUtils;

public class Mods {
    private static ArrayList<IMod> loaded = new ArrayList<IMod>();

    public static void findAndLoad() {
        System.err.println("EL Mod System starting...");
        if (!injectModsFolder()) {
            System.err
                    .println("[WARNING] Mods folder does not exist or is a file, "
                            + "add it if you want mods to be loaded from there.");

            return;
        }
        ArrayList<IMod> injected = ModInjector.findAndInject();
        System.err.println("Loaded mods from classpath.");
        System.err.println("Letting game mess with mods list...");
        KMain inst = KMain.getInst();
        inst.loadMods(injected);
        System.err.println("Complete.");
        loaded.addAll(injected);
        System.err.println("Initializing mods...");
        for (IMod m : loaded) {
            try {
                m.init(inst);
            } catch (Exception e) {
                System.err.println("Error registering mod " + m.getClass()
                        + ":");
                e.printStackTrace();
                injected.remove(m);
            }
        }
        System.err.println("Complete.");
        loaded = injected;
        loaded.trimToSize();
        System.err.println("EL Mod System loaded.");
    }

    private static boolean injectModsFolder() {
        String topLevel = LUtils.TOP_LEVEL;
        File mods = new File(topLevel, "mods");
        if (!mods.exists() || mods.isFile()) {
            return false;
        }

        File[] files = mods.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                // add support for recursive directory searching later
                continue;
            }
            try {
                ClassPathHack.addFile(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.err.println("Injected '" + mods + "' into classpath.");
        return true;
    }

    public static void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
        for (IMod m : loaded) {
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> tmp = new HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>>();
            m.registerRenders(tmp);
            classToRender.putAll(tmp);
        }
    }
}
