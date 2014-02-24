package emergencylanding.k.library.debug;

import emergencylanding.k.library.internalstate.EntityCollide;
import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.lwjgl.tex.ColorTexture;
import emergencylanding.k.library.lwjgl.tex.ELTexture;

public class TestCollisionEntity extends EntityCollide {

	public TestCollisionEntity(World w, float posX, float posY, float posZ) {
		super(w, posX, posY, posZ, ColorTexture.RED);
	}

}
