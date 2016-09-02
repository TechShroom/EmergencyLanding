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
package com.techshroom.emergencylanding.library.internalstate.world;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.techshroom.emergencylanding.library.lwjgl.render.RenderManager;

public class WorldManager {

    private static ArrayList<World> worlds = new ArrayList<World>();
    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static Lock read = WorldManager.rwLock.readLock(), write = WorldManager.rwLock.writeLock();

    public static void addWorldToSystem(World w) {
        WorldManager.write.lock();
        WorldManager.worlds.add(w);
        WorldManager.write.unlock();
    }

    public static void render(int delta) {
        WorldManager.read.lock();
        for (World w : WorldManager.worlds) {
            RenderManager.doRender(w, delta);
        }
        WorldManager.read.unlock();
    }

    public static void update(float delta) {
        WorldManager.read.lock();
        for (World w : WorldManager.worlds) {
            w.update(delta);
        }
        WorldManager.read.unlock();
    }

    public static void interpolate(int delta) {
        WorldManager.read.lock();
        for (World w : WorldManager.worlds) {
            w.interpolate(delta);
        }
        WorldManager.read.unlock();
    }
}
