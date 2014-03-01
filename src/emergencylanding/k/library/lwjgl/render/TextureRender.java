package emergencylanding.k.library.lwjgl.render;

import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.lwjgl.Shapes;
import emergencylanding.k.library.util.DrawableUtils;

public class TextureRender extends Render<ELEntity> {

    @Override
    public void doRender(ELEntity entity, float posX, float posY, float posZ) {
        DrawableUtils.glBeginTrans(posX, posY, posZ);
        VBAO quad = Shapes.getQuad(new VertexData(), new VertexData().setXYZ(
                (float) entity.getTex().getWidth(), (float) entity.getTex()
                        .getHeight(), posZ), Shapes.XY);
        quad.setTexture(entity.getTex());
        quad.draw();
        DrawableUtils.glEndTrans();
    }

}
