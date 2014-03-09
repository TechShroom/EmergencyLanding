package emergencylanding.k.exst.modules;

import java.lang.reflect.Array;
import java.util.*;

import emergencylanding.k.exst.mods.IMod;
import emergencylanding.k.exst.mods.Mods;

public final class ModuleSystem {

    private static ArrayList<IModule> modules = new ArrayList<IModule>();

    private ModuleSystem() {
        throw new AssertionError("Please don't create the module system.");
    }

    public static void loadModulesFromMods() {
        List<IMod> loaded = Mods.getLoadedMods();
        for (IMod m : loaded) {
            if (m instanceof IModule) {
                IModule im = (IModule) m;
                modules.add(im);
            }
        }
    }

    /**
     * Gets the registered modules for the given binary class name.
     * 
     * @param fullBinaryName
     *            - the full binary name for the parent class
     * @return an array of {@link IModule} instances whose getClass() method
     *         either returns the same class represented by the full binary name
     *         or a class that extends that class.
     */
    public static <T extends IModule> T[] getRegisteredModules(
            Class<T> moduleClass) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(moduleClass, 0);
        if (moduleClass == null) {
            return array;
        }
        ArrayList<IModule> matched = new ArrayList<IModule>();
        for (IModule m : modules) {
            if (moduleClass.isInstance(m)) {
                matched.add(m);
            }
        }
        return matched.toArray(array);
    }
}
