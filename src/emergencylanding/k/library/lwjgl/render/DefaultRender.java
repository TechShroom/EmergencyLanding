package emergencylanding.k.library.lwjgl.render;

import emergencylanding.k.library.internalstate.Entity;
import emergencylanding.k.library.lwjgl.Shapes;

public class DefaultRender extends Render<Entity> {
	VBAO quad = Shapes.getQuad(new VertexData(),
			new VertexData().setXYZ(10, 10, 10), Shapes.XY);

	@Override
	public void doRender(Entity entity, float posX, float posY, float posZ) {
		quad.setTexture(entity.getTex());
		// using the already created Victor is more efficient than making a new
		// one from the vertexes
		quad.setXYZOff(entity.getInterpolated());
		quad.draw();
	}

}
