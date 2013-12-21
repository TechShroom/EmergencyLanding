package emergencylanding.k.library.lwjgl.render;

import emergencylanding.k.library.internalstate.Entity;
import emergencylanding.k.library.internalstate.Victor;

public abstract class Render<T extends Entity> {
    /**
     * Renders the given entity at it's location, yaw, and pitch.
     * 
     * @param entity
     */
    public void doRender(T entity, Victor interpolated) {
	doRender(entity, interpolated.x, interpolated.y, interpolated.z);
    }

    /**
     * Renders an entity with the <i>given</i> arguments instead of the
     * entity's.
     * 
     * @param entity
     *            - the entity to render
     * @param posX
     *            - the positionX to render at
     * @param posY
     *            - the positionY to render at
     * @param posZ
     *            - the positionZ to render at
     */
    public abstract void doRender(T entity, float posX, float posY, float posZ);
}
