package com.techshroom.emergencylanding.library.lwjgl.render;

import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.util.DrawableUtils;
import com.techshroom.emergencylanding.library.util.LUtils;

public class TextureRender extends Render<ELEntity> {

    @Override
    public void doRender(ELEntity entity, float posX, float posY, float posZ) {
        DrawableUtils.beginStandardEntityRender(entity, posX, posY, posZ);
        VBAO quad = Shapes.getQuad(new VertexData(),
                new VertexData().setXYZ((float) entity.getTex().getWidth(),
                        (float) entity.getTex().getHeight(), 0),
                Shapes.XY);
        quad.setTexture(entity.getTex());
        quad.setStatic(false);
        if (quad.draw() == null && LUtils.debugLevel >= 1) {
            System.err.println(
                    "Failed to draw " + entity + ", quad said it was invalid");
        }
        quad.destroy();
        DrawableUtils.endStandardEntityRender();
    }

}
