package emergencylanding.k.library.internalstate;

import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.lwjgl.tex.Texture;

public class EntityCollide extends GravEntity {

	public EntityCollide(World w, float posX, float posY, float posZ,
			Texture texture) {
		super(w, posX, posY, posZ, texture);
	}

	public EntityCollide(World w, float posX, float posY, float posZ,
			Texture texture, float gravity) {
		super(w, posX, posY, posZ, texture, gravity);
	}

	@Override
	public void updateOnTick(float delta) {
		super.updateOnTick(delta);
		for (Entity other : w.getEntityList()) {

		}
	}

}
