package com.techshroom.emergencylanding.library.debug;

import com.techshroom.emergencylanding.imported.Color;
import com.techshroom.emergencylanding.library.internalstate.GravEntity;
import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.lwjgl.tex.ColorTexture;

public class TestGravEntity extends GravEntity {

    private boolean bounce;

    public TestGravEntity(World w) {
        super(w, 100, 800, 0, new ColorTexture(Color.RED), 0.1f);
    }

    @Override
    public void updateOnTick(float delta) {
        if (this.pos.y <= 0 && !this.bounce) {
            this.bounce = true;
            setYVel(-this.vel.y);
        }
        if (this.bounce && this.pos.y > 0) {
            this.bounce = false;
        }
        super.updateOnTick(delta);
    }

}
