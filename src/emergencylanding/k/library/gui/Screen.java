package emergencylanding.k.library.gui;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Screen extends GuiElement {
    protected ArrayList<GuiElement> elements = new ArrayList<GuiElement>();
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock read = rwLock.readLock();
    private ReentrantReadWriteLock.WriteLock write = rwLock.writeLock();

    public Screen() {
        super(0, 0);
    }

    public Screen(int x, int y) {
        super(x, y);
    }

    public void addElement(GuiElement element) {
        write.lock();
        elements.add(element);
        write.unlock();
    }

    @Override
    public void updateAt(float x, float y) {
        read.lock();
        for (GuiElement element : elements) {
            element.updateAt(element.xPos + x, element.yPos + y);
        }
        read.unlock();
    }

    @Override
    public void drawAt(float x, float y) {
        read.lock();
        for (GuiElement element : elements) {
            element.drawAt(element.xPos + x, element.yPos + y);
        }
        read.unlock();
    }
}
