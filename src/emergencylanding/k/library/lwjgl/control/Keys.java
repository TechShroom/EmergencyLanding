package emergencylanding.k.library.lwjgl.control;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import emergencylanding.k.imported.Sync;
import emergencylanding.k.library.util.LUtils;

/**
 * A class that uses the AWT event system for callbacks on {@link KeyEvent
 * KeyEvents}. Does not call the {@link KeyListener#keyTyped(KeyEvent)} method.
 * 
 * @author Kenzie Togami
 * 
 */
public class Keys {

    private static ArrayList<KeyListener> listeners = new ArrayList<KeyListener>();
    private static ArrayList<KeyEvent> q = new ArrayList<KeyEvent>();
    private static Object l_l = new Object();
    private static Object q_l = new Object();
    @SuppressWarnings("serial")
    private static Component emptyComponent_nonnull = new Component() {
    };

    static {
        try {
            if (!Display.isCreated()) {
                throw new IllegalStateException(
                        "Display not created before Keyboard");
            }
            Keyboard.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(42);
        }
    }

    /**
     * Read the Keyboard queue
     */
    public static void read() {
        synchronized (q_l) {
            try {
                while (Keyboard.isCreated() && Keyboard.next()) {
                    boolean state = Keyboard.getEventKeyState();
                    q.add(new KeyEvent(emptyComponent_nonnull,
                            (state ? KeyEvent.KEY_PRESSED
                                    : KeyEvent.KEY_RELEASED), System
                                    .currentTimeMillis(), 0, Keyboard
                                    .getEventKey(), Keyboard
                                    .getEventCharacter()));
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * Add a new listener for receiving events
     * 
     * @param k
     *            - the {@link KeyListener} for the events to be received on
     */
    public static void registerListener(KeyListener k) {
        synchronized (l_l) {
            listeners.add(k);
            LUtils.print("Registered new KeyListener " + k);
        }
    }

    private static void fireEvent(KeyEvent keyEvent) {
        synchronized (l_l) {
            for (KeyListener l : listeners) {
                if (keyEvent.getID() == KeyEvent.KEY_PRESSED) {
                    l.keyPressed(keyEvent);
                } else {
                    l.keyReleased(keyEvent);
                }
            }
        }
    }

    static {
        Thread t = new Thread(new Runnable() {

            Sync keySync = new Sync();
            
            @Override
            public void run() {
                while (Keyboard.isCreated()) {
                    keySync.sync(1000);
                    synchronized (q_l) {
                        for (KeyEvent ke : q) {
                            fireEvent(ke);
                        }
                        q.clear();
                    }
                }
            }
        }, "Key Event Firing For EmergencyLanding");
        t.setDaemon(true);
        t.start();
    }
}
