package com.techshroom.emergencylanding.library.internalstate;

import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;

public class GravEntity extends ELEntity {

    private float grav;

    public GravEntity(World w, float posX, float posY, float posZ,
            ELTexture texture) {
        super(w, posX, posY, posZ, texture);
        this.grav = 9.8f;
    }

    /**
     * 
     * @param posX
     *            -x position
     * @param posY
     *            -y position
     * @param posZ
     *            -z position
     * @param texture
     *            -texture, duh.
     * @param gravity
     *            -Acceleration due to gravity
     */
    public GravEntity(World w, float posX, float posY, float posZ,
            ELTexture texture, float gravity) {
        super(w, posX, posY, posZ, texture);
        this.grav = gravity;
    }

    @Override
    public void updateOnTick(float delta) {
        super.updateOnTick(delta);
        setRelativeXYZVel(0, -this.grav, 0);
    }
}
