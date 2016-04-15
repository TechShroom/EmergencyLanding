package com.techshroom.emergencylanding.library.util;

import java.util.concurrent.TimeUnit;

import org.lwjgl.glfw.GLFW;

import com.google.common.base.Ticker;

public final class GLFWTicker extends Ticker {

    public static final GLFWTicker INSTANCE = new GLFWTicker();
    private static final double SECONDS_TO_NANOSECONDS =
            TimeUnit.SECONDS.toNanos(1);

    private GLFWTicker() {
    }

    @Override
    public long read() {
        return (long) (GLFW.glfwGetTime() * SECONDS_TO_NANOSECONDS);
    }

}
