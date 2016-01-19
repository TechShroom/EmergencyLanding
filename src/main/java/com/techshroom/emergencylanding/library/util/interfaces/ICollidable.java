package com.techshroom.emergencylanding.library.util.interfaces;

import com.techshroom.emergencylanding.library.util.BoundingBox;

/**
 * ICollidable provides an interface over which many different objects can agree
 * on collisions. It can be applied for entities, buttons, and more.
 * 
 * @author Kenzie Togami
 *
 */
public interface ICollidable<T> {

    /**
     * Gets the {@link BoundingBox} that can be used for collisions.
     * 
     * @return the rotation and coordinate data in one concise object
     */
    public BoundingBox getBB();

    /**
     * Determines if this ICollidable collides with the other ICollidable. Most
     * implementations should call upon the {@link #getBB()} method and use the
     * returned {@link BoundingBox} to calculate intersection via
     * {@link BoundingBox#collidesWith(ICollidable)}
     * 
     * @param other
     *            - the ICollidable to check collision against
     * @return true if this ICollidable collides with the other ICollidable
     */
    public boolean collidesWith(ICollidable<? extends ICollidable<?>> other);

    /**
     * Implementations should flesh this out this if they use special handling
     * for themselves.
     * 
     * @param other
     *            - the T instance to check against
     * @return true if this T collides with the other T
     */
    public boolean collidesWith(T other);
}
