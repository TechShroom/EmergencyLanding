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
