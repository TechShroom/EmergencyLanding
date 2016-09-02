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
