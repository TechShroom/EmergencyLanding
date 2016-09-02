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

import com.flowpowered.math.vector.Vector2i;
import com.techshroom.emergencylanding.imported.Sync;
import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.internalstate.world.WorldManager;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.lwjgl.render.RenderManager;
import com.techshroom.emergencylanding.library.lwjgl.render.VBAO;
import com.techshroom.emergencylanding.library.lwjgl.render.VertexData;
import com.techshroom.emergencylanding.library.main.KMain;
import com.techshroom.emergencylanding.library.util.DrawableUtils;
import com.techshroom.emergencylanding.library.util.LUtils;

public class WorldAndEntityTest extends KMain {

    private static DisplayLayer layer;
    private static Thread is, ip;
    private static boolean run = true;
    private static final int TICKS_PER_SECOND = 60;
    private static final int FRAMES_PER_SECOND = 60;

    public static void main(String[] args) {
        try {
            layer = DisplayLayer.initDisplay(0, 800, 500,
                    "Testing " + LUtils.LIB_NAME, false, true, args);
            layer.getDisplayFPSTracker().enable(layer.getWindow());
            WorldAndEntityTest.startISThreads();
            // This needs to wait until the windows shows up
            while (WorldAndEntityTest.run) {
                WorldAndEntityTest.run = !layer.shouldClose();
                layer.loop(FRAMES_PER_SECOND);
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

    private static void startISThreads() {
        Runnable isr = new Runnable() {

            Sync s = new Sync();

            @Override
            public void run() {
                FPS fps = new FPS("is");
                while (WorldAndEntityTest.run) {
                    this.s.sync(WorldAndEntityTest.TICKS_PER_SECOND);
                    int delta = fps.update();
                    WorldManager.update(delta);
                }
            }
        };

        WorldAndEntityTest.is = new Thread(isr);
        WorldAndEntityTest.is.setName("Internal State Thread");
        WorldAndEntityTest.is.start();

        Runnable ipr = new Runnable() {

            Sync s = new Sync();

            @Override
            public void run() {
                FPS fps = new FPS("ip");
                while (WorldAndEntityTest.run) {
                    this.s.sync(WorldAndEntityTest.FRAMES_PER_SECOND);
                    int delta = fps.update();
                    WorldManager.interpolate(delta);
                }
            }
        };

        WorldAndEntityTest.ip = new Thread(ipr);
        WorldAndEntityTest.ip.setName("Interpolation Thread");
        WorldAndEntityTest.ip.setDaemon(true);
        WorldAndEntityTest.ip.start();
        System.err.println("ISThreads running!");
    }

    private World w;

    private VBAO quad;

    @Override
    public void onDisplayUpdate(int delta) {
        double yaw = this.w.getEntityList().get(0).getYaw();
        DrawableUtils.glBeginRot(yaw, 0, 1, 0);
        this.quad.draw();
        DrawableUtils.glEndRot();
        RenderManager.render(delta);
    }

    @Override
    public void init(DisplayLayer layer, String[] args) {
        this.w = new World();
        WorldManager.addWorldToSystem(this.w);
        for (int i = 0; i < 500; i++) {
            ELEntity e = new TestEntity(this.w);
            this.w.addEntity(e);
            e.setXYZ(0, 0, 0);
            e.setXYZVel(1, 1, 0);
        }
        Vector2i size = DrawableUtils.getWindowSize(layer);
        this.quad = Shapes.getQuad(new VertexData().setRGB(255, 255, 255),
                new VertexData().setXYZ(size.getX(), size.getY(), 0),
                Shapes.XY);
        this.quad.setStatic(false);
    }

}
