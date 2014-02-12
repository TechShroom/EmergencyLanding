package emergencylanding.k.exst.mods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceLoader;

final class ModInjector {

    /**
     * Scans the classpath and injects any mods it can find into the game. The
     * list is used by {@link Mods} to get the initial listings.
     * 
     * @return a list of the injected mods
     */
    static ArrayList<IMod> findAndInject() {
        ArrayList<IMod> in = new ArrayList<IMod>();
        ServiceLoader<IMod> loader = ServiceLoader.load(IMod.class);
        Iterator<IMod> iter = loader.iterator();
        while (iter.hasNext()) {
            in.add(iter.next());
        }
        return in;
    }
}
