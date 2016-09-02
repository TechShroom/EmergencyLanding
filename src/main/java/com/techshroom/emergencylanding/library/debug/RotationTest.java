/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <https://techshoom.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
