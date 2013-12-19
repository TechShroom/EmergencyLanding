package emergencylanding.k.library.debug;

import emergencylanding.k.library.internalstate.GravEntity;
import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.lwjgl.tex.ColorTexture;

public class TestGravEntity extends GravEntity {

	private boolean bounce;

	public TestGravEntity(World w) {
		super(w, 100, 800, 0, ColorTexture.RED, 0.1f);
	}

	@Override
	public void updateOnTick(float delta) {
		if (pos.y <= 0 && !bounce) {
			bounce = true;
			setYVel(-vel.y);
		}
		if (bounce && pos.y > 0) {
			bounce = false;
		}
		super.updateOnTick(delta);
	}

}
