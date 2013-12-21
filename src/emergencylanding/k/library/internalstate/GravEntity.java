package emergencylanding.k.library.internalstate;

import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.lwjgl.tex.Texture;

public class GravEntity extends Entity {

    private float grav;

    public GravEntity(World w, float posX, float posY, float posZ,
	    Texture texture) {
	super(w, posX, posY, posZ, texture);
	grav = 9.8f;
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
	    Texture texture, float gravity) {
	super(w, posX, posY, posZ, texture);
	grav = gravity;
    }

    @Override
    public void updateOnTick(float delta) {
	super.updateOnTick(delta);
	setRelativeXYZVel(0, -grav, 0);
    }
}
