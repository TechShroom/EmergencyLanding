package emergencylanding.k.library.gui;

public abstract class GuiElement {

	protected float xPos, yPos;

	/**
	 * Constructor.
	 * 
	 * @param x
	 *            -The x position of the GUI element.
	 * 
	 * @param y
	 *            -The y position of the GUI element.
	 */
	public GuiElement(float x, float y) {
		xPos = x;
		yPos = y;
	}

	/**
	 * Set the X position of this GUI element.
	 * 
	 * @param newX
	 *            -The x position of the GUI element.
	 */
	public void setXPos(float newX) {
		xPos = newX;
	}

	/**
	 * Set the Y position of this GUI element.
	 * 
	 * @param newY
	 *            -The y position of the GUI element.
	 */
	public void setYPos(float newY) {
		yPos = newY;
	}

	/**
	 * Returns the x position of this GUI element.
	 * 
	 * @return -The x position of this GUI element.
	 */
	public float getXPos() {
		return xPos;
	}

	/**
	 * Returns the y position of this GUI element.
	 * 
	 * @return -The y position of this GUI element.
	 */
	public float getYPos() {
		return yPos;
	}

	public void draw() {
		drawAt(this.xPos, this.yPos);
	}

	public void update() {
		updateAt(this.xPos, this.yPos);
	}

	public abstract void drawAt(float x, float y);

	public abstract void updateAt(float x, float y);

}
