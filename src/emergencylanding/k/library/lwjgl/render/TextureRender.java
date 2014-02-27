package emergencylanding.k.library.lwjgl.render;

import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.lwjgl.Shapes;
import emergencylanding.k.library.util.DrawableUtils;
import emergencylanding.k.library.util.GLRotator;

public class TextureRender extends Render<ELEntity> {

    @Override
    public void doRender(ELEntity entity, float posX, float posY,
            float posZ) {
        GLRotator g = DrawableUtils.newRotater();
        g.glBeginRot(45, 1, 0, 0);
        VBAO quad = Shapes.getQuad(new VertexData(),
                new VertexData().setXYZ((float) entity.getTex().getWidth(),
                        (float) entity.getTex().getHeight(), 0), Shapes.XY);
        quad.setTexture(entity.getTex());
        quad.setXYZOff(entity.getInterpolated());
        quad.draw();
        g.glEndRot();
    }

}
