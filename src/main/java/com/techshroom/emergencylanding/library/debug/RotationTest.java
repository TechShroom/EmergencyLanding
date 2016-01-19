package com.techshroom.emergencylanding.library.debug;

import java.util.HashMap;

import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.render.Render;
import com.techshroom.emergencylanding.library.lwjgl.render.RenderManager;
import com.techshroom.emergencylanding.library.lwjgl.render.TextureRender;
import com.techshroom.emergencylanding.library.main.KMain;
import com.techshroom.emergencylanding.library.util.DrawableUtils;

public class RotationTest extends KMain {

    private static DisplayLayer layer;
    private static boolean run = true;
    TestCollisionEntity tce, tce2;

    public static void main(String[] args) {
        try {
            layer = DisplayLayer.initDisplay(0, 800, 500, "Testing Rotations",
                    false, true, args);
            System.err.println("loaded");
            while (run) {
                run = !layer.shouldClose();
                layer.loop(120);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (layer != null) {
                layer.destroy();
            }
            DisplayLayer.finalExitCall();
        }
    }

    @Override
    public void onDisplayUpdate(int delta) {
        DrawableUtils.glBeginScale(.8, .8, 1);
        this.tce.interpolate(delta);
        RenderManager.doRender(this.tce, this.tce.getInterpolated());
        DrawableUtils.glEndScale();
        this.tce2.interpolate(delta);
        RenderManager.doRender(this.tce2, this.tce2.getInterpolated());
        this.tce.setYaw(this.tce.getYaw() + (delta * 0.2));
        double oldRoll = this.tce2.getRoll();
        this.tce2.setRoll(this.tce2.getPitch() + (delta * 0.1));
        this.tce2.setPitch(oldRoll + (delta * 0.1));
    }

    @Override
    public void init(DisplayLayer layer, String[] args) {
        this.tce = new TestCollisionEntity(new World(), 200, 123, 0);
        this.tce2 = new TestCollisionEntity(new World(), 123, 123, 0);
        this.tce2.setPitch(0);
        this.tce.setPitch(45);
        this.tce.setRoll(10);
    }

    @Override
    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
        classToRender.put(TestCollisionEntity.class, new TextureRender());
    }

}
