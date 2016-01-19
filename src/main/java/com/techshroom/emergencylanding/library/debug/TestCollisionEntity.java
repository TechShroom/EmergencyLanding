package com.techshroom.emergencylanding.library.debug;

import com.techshroom.emergencylanding.library.internalstate.EntityCollide;
import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.util.DrawableUtils;
import com.techshroom.emergencylanding.library.util.LUtils;

public class TestCollisionEntity extends EntityCollide {

    public TestCollisionEntity(World w, float posX, float posY, float posZ) {
        super(w, posX, posY, posZ, DrawableUtils
                .getTextureFromFile(LUtils.getELTop() + "/help.png"));
    }

}
