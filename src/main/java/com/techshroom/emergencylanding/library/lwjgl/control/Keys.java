package com.techshroom.emergencylanding.library.lwjgl.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * A class that uses the AWT event system for callbacks on {@link KeyEvent
 * KeyEvents}. Does not call the {@link KeyListener#keyTyped(KeyEvent)} method.
 * 
 * @author Kenzie Togami
 * 
 */
public final class Keys implements GLFWKeyCallback.SAM {

    private static final Map<Long, Keys> helperMap = new HashMap<>();

    public static Keys getHelper(long window) {
        Keys help = helperMap.computeIfAbsent(window, Keys::new);
        return help;
    }

    private final GLFWKeyCallback thisKCB = GLFWKeyCallback.create(this);

    private Keys(long window) {
        GLFW.glfwSetKeyCallback(window, this.thisKCB);
    }

    @Override
    public void invoke(long window, int key, int scancode, int action,
            int mods) {
    }

}
