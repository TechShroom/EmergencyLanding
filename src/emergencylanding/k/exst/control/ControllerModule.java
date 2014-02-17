package emergencylanding.k.exst.control;

import emergencylanding.k.exst.modules.IModule;
import emergencylanding.k.library.main.KMain;

public class ControllerModule extends IModule {

    public ControllerModule() {
    }

    @Override
    public void init(KMain instance) {
        System.err.println("Loaded Controllers via Mods interface.");
    }

}
