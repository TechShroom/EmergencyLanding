/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <http://techshoom.com>
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

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Screen extends GuiElement {

    public final long window;
    protected ArrayList<GuiElement> elements = new ArrayList<GuiElement>();
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock read = this.rwLock.readLock();
    private ReentrantReadWriteLock.WriteLock write = this.rwLock.writeLock();

    public Screen(long window) {
        this(window, 0, 0);
    }

    public Screen(long window, int x, int y) {
        super(x, y);
        this.window = window;
    }

    public void addElement(GuiElement element) {
        this.write.lock();
        this.elements.add(element);
        element.setParent(element);
        this.write.unlock();
    }

    @Override
    public void update() {
        this.read.lock();
        for (GuiElement element : this.elements) {
            element.update();
        }
        this.read.unlock();
    }

    @Override
    public void draw() {
        this.read.lock();
        for (GuiElement element : this.elements) {
            element.draw();
        }
        this.read.unlock();
    }

}
