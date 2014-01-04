package emergencylanding.k.library.internalstate.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import emergencylanding.k.library.internalstate.ELEntity;

public class World {
    private static int DEF_WORLD_SIZE = 800;

    protected ArrayList<ELEntity> loadedEntities = new ArrayList<ELEntity>();
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock read = rwLock.readLock();
    private ReentrantReadWriteLock.WriteLock write = rwLock.writeLock();

    /**
     * Width = MAX_X Height = MAX_Y Length = MAX_Z
     */
    public int width, height, length;

    public World() {
        this(DEF_WORLD_SIZE, DEF_WORLD_SIZE, DEF_WORLD_SIZE);
    }

    public World(int w, int h, int l) {
        width = w;
        height = h;
        length = l;
    }

    public void setSize(int newW, int newH, int newL) {
        width = newW;
        height = newH;
        length = newL;
    }

    /**
     * Returns an unmodifiable List for access to the entities.
     * 
     * @return an unmodifiable List of entities in this world
     */
    public List<ELEntity> getEntityList() {
        read.lock();
        List<ELEntity> unmodlist = Collections
                .unmodifiableList(new ArrayList<ELEntity>(loadedEntities));
        read.unlock();
        return unmodlist;
    }

    /**
     * Adds <i>e</i> to the list
     * 
     * @param e
     *            - the entity to add
     */
    public void addEntity(ELEntity e) {
        write.lock();
        loadedEntities.add(e);
        write.unlock();
    }

    /**
     * Updates all entities with given delta
     * 
     * @param delta
     *            - time since last loop in ms
     */
    public void update(float delta) {
        ArrayList<ELEntity> removeList = new ArrayList<ELEntity>();
        read.lock();
        for (ELEntity e : loadedEntities) {
            if (e.isDead()) {
                removeList.add(e);
                continue;
            }
            e.updateOnTick(delta);
        }
        read.unlock();
        write.lock();
        loadedEntities.removeAll(removeList);
        write.unlock();
    }

    /**
     * Interpolates all entities with given delta
     * 
     * @param delta
     *            - time since last loop in ms
     */
    public void interpolate(int delta) {
        read.lock();
        for (ELEntity e : loadedEntities) {
            e.interpolate(delta);
        }
        read.unlock();
    }

    @Override
    public String toString() {
        return String.format(
                "{World:{Entities:%s,width:%s,height:%s,length:%s}}",
                loadedEntities, width, height, length);
    }
}
