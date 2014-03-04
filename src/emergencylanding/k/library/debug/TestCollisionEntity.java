package emergencylanding.k.library.debug;

import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.util.DrawableUtils;
import emergencylanding.k.library.util.LUtils;

public class TestCollisionEntity extends ELEntity {

    public TestCollisionEntity(World w, float posX, float posY, float posZ) {
        super(w, posX, posY, posZ, DrawableUtils.getTextureFromFile(LUtils
                .getELTop() + "/res/help.png"));
    }

}
