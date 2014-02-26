package emergencylanding.k.library.lwjgl.render;

import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.internalstate.EntityCollide;
import emergencylanding.k.library.lwjgl.Shapes;

public class TextureRender extends Render<EntityCollide> {

    @Override
    public void doRender(EntityCollide entity, float posX, float posY, float posZ) {

        VBAO quad = Shapes.getQuad(new VertexData(), 
                new VertexData().setXYZ((float)entity.getxLen(), (float)entity.getyLen(),0), Shapes.XY);
        quad.setTexture(entity.getTex());
        quad.setXYZOff(entity.getInterpolated());
        quad.draw();
    }

}
