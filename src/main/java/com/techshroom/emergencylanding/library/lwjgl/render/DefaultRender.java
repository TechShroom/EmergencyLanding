package com.techshroom.emergencylanding.library.lwjgl.render;

import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.util.DrawableUtils;

public class DefaultRender extends Render<ELEntity> {

    VBAO quad = Shapes.getQuad(new VertexData(),
            new VertexData().setXYZ(10, 10, 10), Shapes.XY);
    {
        this.quad.setStatic(false);
    }

    @Override
    public void doRender(ELEntity entity, float posX, float posY, float posZ) {
        DrawableUtils.beginStandardEntityRender(entity, posX, posY, posZ);
        this.quad.setTexture(entity.getTex());
        this.quad.draw();
        DrawableUtils.endStandardEntityRender();
    }

}
