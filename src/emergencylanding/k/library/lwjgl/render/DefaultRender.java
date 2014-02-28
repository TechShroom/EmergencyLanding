package emergencylanding.k.library.lwjgl.render;

import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.lwjgl.Shapes;

public class DefaultRender extends Render<ELEntity> {
    VBAO quad = Shapes.getQuad(new VertexData(),
            new VertexData().setXYZ(10, 10, 10), Shapes.XY);

    @Override
    public void doRender(ELEntity entity, float posX, float posY, float posZ) {
        quad.setTexture(entity.getTex());
        // using the already created Victor is more efficient than making a new
        // one from the vertexes, as this is the default renderer and should not
        // be used it is okay to break things :)
        quad.setXYZOff(entity.getInterpolated());
        quad.draw();
    }

}
