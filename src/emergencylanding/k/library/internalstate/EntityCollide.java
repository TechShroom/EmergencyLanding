package emergencylanding.k.library.internalstate;

import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.lwjgl.tex.ELTexture;

public class EntityCollide extends GravEntity {

    double xLen, yLen;

    public EntityCollide(World w, float posX, float posY, float posZ,
            ELTexture texture) {
        super(w, posX, posY, posZ, texture);
        xLen = texture.getWidth();
        yLen = texture.getHeight();
    }

    public EntityCollide(World w, float posX, float posY, float posZ,
            ELTexture texture, float gravity) {
        super(w, posX, posY, posZ, texture, gravity);
        xLen = texture.getWidth();
        yLen = texture.getHeight();
    }

    @Override
    public void updateOnTick(float delta) {
        super.updateOnTick(delta);
        for (ELEntity other : w.getEntityList()) {
            if (other instanceof EntityCollide) {
                testCollide((EntityCollide) other);
            }
        }
    }

    private boolean testCollide(EntityCollide other) {
        return false;
    }

}
