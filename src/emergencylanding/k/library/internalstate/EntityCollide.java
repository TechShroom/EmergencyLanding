package emergencylanding.k.library.internalstate;

import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.lwjgl.tex.ELTexture;

public abstract class EntityCollide extends ELEntity {

    private double xLen, yLen;

	public EntityCollide(World w, float posX, float posY, float posZ,
            ELTexture texture) {
        super(w, posX, posY, posZ, texture);
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

    public boolean testCollide(EntityCollide other) {
    	double xCenterThis = this.getX() + this.getxLen();
    	double yCenterThis = this.getY() + this.getyLen();
    	double xCenterOther = other.getX() + other.getxLen();
    	double yCenterOther = other.getY() + other.getyLen();
    	
    	double gapX = xCenterOther-xCenterThis;
    	double gapY = yCenterOther-yCenterThis;
    	
    	double angleToOther = Math.atan2(gapY, gapX);
    	
    	double x_newGap = EntityCollide.projectLineAlongSurface(this.yaw, angleToOther, 
    														Math.sqrt(gapX*gapX + gapY*gapY),false);
    	double y_newGap = EntityCollide.projectLineAlongSurface(this.yaw, angleToOther, 
    														Math.sqrt(gapX*gapX + gapY*gapY),true);
    	
    	double thisXLenOnNewGrid = this.getxLen();
    	double thisYLenOnNewGrid = this.getyLen();
    	
    	double otherXLenOnNewGrid =  EntityCollide.projectLineAlongSurface(this.yaw, other.yaw, 
				other.getxLen(),false) +
				EntityCollide.projectLineAlongSurface(this.yaw, other.yaw, 
				other.getyLen(),true);
    	double otherYLenOnNewGrid =  EntityCollide.projectLineAlongSurface(this.yaw, other.yaw, 
				other.getxLen(),true) +
				EntityCollide.projectLineAlongSurface(this.yaw, other.yaw, 
				other.getyLen(),false);
    	
    	return (x_newGap < thisXLenOnNewGrid+otherXLenOnNewGrid && y_newGap < thisYLenOnNewGrid+otherYLenOnNewGrid);
    }
    
    /**
     * Finds the length of a line when projected along another line. Used for collisions.
     * @param thetaSurface
     * 		- The angle of the surface to be projected upon makes with the horizontal
     * @param thetaLineToProject
     * 		- The angle the line makes with the horizontal
     * @param magnitude
     * 		- The length of the line.
     * @param perpendicular
     * 		- Gets the length of the line when projected along the line perpendicular to the thetaSurface given (eg, find the y).
     */
    public static double projectLineAlongSurface(double thetaSurface, double thetaLineToProject, double magnitude, boolean perpendicular)
    {
    	double theta = thetaLineToProject-thetaSurface;
    	if(perpendicular)
    	{
    		theta+=90;
    	}
    	return Math.cos(Math.toRadians(theta))*magnitude;
    }

	/**
	 * @return the xLen
	 */
	public double getxLen() {
		return xLen;
	}

	/**
	 * @param xLen the xLen to set
	 */
	public void setxLen(double xLen) {
		this.xLen = xLen;
	}

	/**
	 * @return the yLen
	 */
	public double getyLen() {
		return yLen;
	}

	/**
	 * @param yLen the yLen to set
	 */
	public void setyLen(double yLen) {
		this.yLen = yLen;
	}
}