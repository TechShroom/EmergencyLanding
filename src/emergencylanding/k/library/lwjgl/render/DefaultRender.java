package emergencylanding.k.library.lwjgl.render;

import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.lwjgl.Shapes;
import emergencylanding.k.library.util.DrawableUtils;

public class DefaultRender extends Render<ELEntity> {
    VBAO quad = Shapes.getQuad(new VertexData(),
            new VertexData().setXYZ(10, 10, 10), Shapes.XY);

    @Override
    public void doRender(ELEntity entity, float posX, float posY, float posZ) {
        DrawableUtils.glBeginRot(entity.getRoll(), 1, 0, 0);
        DrawableUtils.glBeginRot(entity.getYaw(), 0, 1, 0);
        DrawableUtils.glBeginRot(entity.getPitch(), 0, 0, 1);
        DrawableUtils.glBeginTrans(posX, posY, posZ);
        quad.setTexture(entity.getTex());
        quad.draw();
        DrawableUtils.glEndTrans();
        DrawableUtils.glEndRot();
        DrawableUtils.glEndRot();
        DrawableUtils.glEndRot();
    }

}
