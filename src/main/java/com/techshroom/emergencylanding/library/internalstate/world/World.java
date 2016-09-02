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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.techshroom.emergencylanding.library.internalstate.ELEntity;

public class World {

    private static int DEF_WORLD_SIZE = 800;

    protected ArrayList<ELEntity> loadedEntities = new ArrayList<ELEntity>();
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock read = this.rwLock.readLock();
    private ReentrantReadWriteLock.WriteLock write = this.rwLock.writeLock();

    /**
     * Width = MAX_X Height = MAX_Y Length = MAX_Z
     */
    public int width, height, length;

    public World() {
        this(DEF_WORLD_SIZE, DEF_WORLD_SIZE, DEF_WORLD_SIZE);
    }

    public World(int w, int h, int l) {
        this.width = w;
        this.height = h;
        this.length = l;
    }

    public void setSize(int newW, int newH, int newL) {
        this.width = newW;
        this.height = newH;
        this.length = newL;
    }

    /**
     * Returns an unmodifiable List for access to the entities.
     * 
     * @return an unmodifiable List of entities in this world
     */
    public List<ELEntity> getEntityList() {
        this.read.lock();
        List<ELEntity> unmodlist = Collections
                .unmodifiableList(new ArrayList<ELEntity>(this.loadedEntities));
        this.read.unlock();
        return unmodlist;
    }

    /**
     * Adds <i>e</i> to the list
     * 
     * @param e
     *            - the entity to add
     */
    public void addEntity(ELEntity e) {
        this.write.lock();
        this.loadedEntities.add(e);
        this.write.unlock();
    }

    /**
     * Updates all entities with given delta
     * 
     * @param delta
     *            - time since last loop in ms
     */
    public void update(float delta) {
        ArrayList<ELEntity> removeList = new ArrayList<ELEntity>();
        this.read.lock();
        for (ELEntity e : this.loadedEntities) {
            if (e.isDead()) {
                removeList.add(e);
                continue;
            }
            e.updateOnTick(delta);
        }
        this.read.unlock();
        this.write.lock();
        this.loadedEntities.removeAll(removeList);
        this.write.unlock();
    }

    /**
     * Interpolates all entities with given delta
     * 
     * @param delta
     *            - time since last loop in ms
     */
    public void interpolate(int delta) {
        this.read.lock();
        for (ELEntity e : this.loadedEntities) {
            e.interpolate(delta);
        }
        this.read.unlock();
    }

    @Override
    public String toString() {
        return String.format(
                "{World:{Entities:%s,width:%s,height:%s,length:%s}}",
                this.loadedEntities, this.width, this.height, this.length);
    }
}
