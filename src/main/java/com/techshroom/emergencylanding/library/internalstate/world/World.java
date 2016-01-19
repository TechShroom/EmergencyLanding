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
