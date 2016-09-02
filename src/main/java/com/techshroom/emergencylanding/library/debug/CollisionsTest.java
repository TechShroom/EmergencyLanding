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

import com.techshroom.emergencylanding.imported.Sync;
import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.internalstate.world.WorldManager;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.render.Render;
import com.techshroom.emergencylanding.library.lwjgl.render.RenderManager;
import com.techshroom.emergencylanding.library.lwjgl.render.TextureRender;
import com.techshroom.emergencylanding.library.main.KMain;

public class CollisionsTest extends KMain {

    private static Thread is;
    private static boolean run = true;
    private static Thread ip;
    private static final int TICKS_PER_SECOND = 20;
    private static final int FRAMES_PER_SECOND = 1200;

    private static DisplayLayer layer;
    private static World w;
    private static TestCollisionEntity e;
    private static TestCollisionEntity e2;
    private static TestCollisionEntity e3;

    public static void main(String[] args) {
        try {
            layer = DisplayLayer.initDisplay(0, 800, 500, "Testing Collisions", false, false, args);
            layer.getDisplayFPSTracker().enable(layer.getWindow());
            CollisionsTest.startISThreads();
            while (CollisionsTest.run) {
                CollisionsTest.run = !layer.shouldClose();
                layer.loop(CollisionsTest.FRAMES_PER_SECOND);
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
                FPS fps = new FPS("internal-state");
                while (CollisionsTest.run) {
                    this.s.sync(CollisionsTest.TICKS_PER_SECOND);
                    int delta = fps.update();
                    WorldManager.update(delta);
                    if (e.testCollide(e2)) {
                        System.err.println("**BOOM**");
                        run = false;
                    }
                    e.setRelativeXYZ(0, 1, 0);
                }
            }
        };

        CollisionsTest.is = new Thread(isr);
        CollisionsTest.is.setName("Internal State Thread");
        CollisionsTest.is.start();

        Runnable ipr = new Runnable() {

            Sync s = new Sync();

            @Override
            public void run() {
                FPS fps = new FPS("interpolate-state");
                while (CollisionsTest.run) {
                    this.s.sync(CollisionsTest.FRAMES_PER_SECOND);
                    int delta = fps.update();
                    WorldManager.interpolate(delta);
                }
            }
        };

        CollisionsTest.ip = new Thread(ipr);
        CollisionsTest.ip.setName("Interpolation Thread");
        CollisionsTest.ip.setDaemon(true);
        CollisionsTest.ip.start();
        System.err.println("ISThreads running!");
    }

    @Override
    public void onDisplayUpdate(int delta) {
        RenderManager.render(delta);
    }

    @Override
    public void init(DisplayLayer layer, String[] args) {
        w = new World();
        WorldManager.addWorldToSystem(w);
        e = new TestCollisionEntity(w, 20, 250, 50);
        e2 = new TestCollisionEntity(w, 50, 400, 50);
        e.setPitch(30);
        e2.setPitch(45);
        w.addEntity(e);
        w.addEntity(e2);
    }

    @Override
    public void registerRenders(HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
        classToRender.put(TestCollisionEntity.class, new TextureRender());
    }
}
