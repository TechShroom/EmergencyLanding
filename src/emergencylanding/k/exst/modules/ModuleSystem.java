package emergencylanding.k.exst.modules;

import java.util.*;

import emergencylanding.k.exst.mods.IMod;
import emergencylanding.k.exst.mods.Mods;

public final class ModuleSystem {
    private static final Class<IModule> IMODULE_CLASS = IModule.class;

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
    public static IModule[] getRegisteredModules(String fullBinaryName) {
        Class<?> fbnClass = null;
        try {
            fbnClass = Class.forName(fullBinaryName);
        } catch (ClassNotFoundException e1) {
            System.err.println("Module class " + fullBinaryName
                    + " does not exist, returning empty.");
        }
        if (!IMODULE_CLASS.isAssignableFrom(fbnClass)) {
            throw new ClassCastException(fullBinaryName + " does not extend "
                    + IMODULE_CLASS);
        }
        if (fbnClass == null) {
            return new IModule[0];
        }
        ArrayList<IModule> matched = new ArrayList<IModule>();
        for (IModule m : modules) {
            if (fbnClass.isInstance(m)) {
                matched.add(m);
            }
        }
        return matched.toArray(new IModule[0]);
    }
}
