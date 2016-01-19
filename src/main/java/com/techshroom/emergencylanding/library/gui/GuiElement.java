package com.techshroom.emergencylanding.library.gui;

import java.util.OptionalLong;

public abstract class GuiElement {

    private GuiElement parent;
    protected double xPos, yPos;

    /**
     * Constructor.
     * 
     * @param x
     *            -The x position of the GUI element.
     * 
     * @param y
     *            -The y position of the GUI element.
     */
    public GuiElement(double x, double y) {
        this.xPos = x;
        this.yPos = y;
    }

    /**
     * Set the X position of this GUI element.
     * 
     * @param newX
     *            -The x position of the GUI element.
     */
    public void setXPos(double newX) {
        this.xPos = newX;
    }

    /**
     * Set the Y position of this GUI element.
     * 
     * @param newY
     *            -The y position of the GUI element.
     */
    public void setYPos(double newY) {
        this.yPos = newY;
    }

    /**
     * Returns the x position of this GUI element.
     * 
     * @return -The x position of this GUI element.
     */
    public double getXPos() {
        return this.xPos;
    }

    /**
     * Returns the y position of this GUI element.
     * 
     * @return -The y position of this GUI element.
     */
    public double getYPos() {
        return this.yPos;
    }

    public void setParent(GuiElement parent) {
        this.parent = parent;
    }

    public GuiElement getParent() {
        return this.parent;
    }

    public boolean isRootElement() {
        return getParent() == null;
    }

    protected OptionalLong getWindowHandle() {
        GuiElement element = this;
        while (element != null) {
            if (element instanceof Screen) {
                return OptionalLong.of(((Screen) element).window);
            }
            element = element.getParent();
        }
        return OptionalLong.empty();
    }

    public abstract void draw();

    public abstract void update();

}
