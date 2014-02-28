package emergencylanding.k.library.internalstate;

import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.lwjgl.tex.ELTexture;
import emergencylanding.k.library.util.Maths;

public abstract class EntityCollide extends ELEntity {

    public EntityCollide(World w, float posX, float posY, float posZ,
            ELTexture texture) {
        super(w, posX, posY, posZ, texture);
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

    public boolean testCollide(EntityCollide other) {
        double xCenterThis = this.getX() + this.getTex().getWidth();
        double yCenterThis = this.getY() + this.getTex().getHeight();
        double xCenterOther = other.getX() + other.getTex().getWidth();
        double yCenterOther = other.getY() + other.getTex().getHeight();

        double gapX = xCenterOther - xCenterThis;
        double gapY = yCenterOther - yCenterThis;

        double angleToOther = Math.atan2(gapY, gapX);

        double x_newGap = Maths.projectLineAlongSurface(this.yaw, angleToOther,
                Math.sqrt(gapX * gapX + gapY * gapY), false);
        double y_newGap = Maths.projectLineAlongSurface(this.yaw, angleToOther,
                Math.sqrt(gapX * gapX + gapY * gapY), true);

        double thisXLenOnNewGrid = this.getTex().getWidth();
        double thisYLenOnNewGrid = this.getTex().getHeight();

        double otherXLenOnNewGrid = Maths.projectLineAlongSurface(this.yaw,
                other.yaw, other.getTex().getWidth(), false)
                + Maths.projectLineAlongSurface(this.yaw, other.yaw,
                        other.getTex().getHeight(), true);
        double otherYLenOnNewGrid = Maths.projectLineAlongSurface(this.yaw,
                other.yaw, other.getTex().getWidth(), true)
                + Maths.projectLineAlongSurface(this.yaw, other.yaw,
                        other.getTex().getHeight(), false);

        System.err.println(y_newGap + " " + thisYLenOnNewGrid + " "
                + otherYLenOnNewGrid);
        return (x_newGap < thisXLenOnNewGrid / 2 + otherXLenOnNewGrid / 2 && y_newGap < thisYLenOnNewGrid
                / 2 + otherYLenOnNewGrid / 2);
    }
}