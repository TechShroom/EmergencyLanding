package com.techshroom.emergencylanding.library.debug;

import java.util.concurrent.TimeUnit;

import org.lwjgl.glfw.GLFW;

import com.techshroom.emergencylanding.library.util.LUtils;

public final class FPS {

    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;

    static {
        LUtils.init(); // init if not done already, otherwise errors
    }

    private long window;
    private long lastFPS;
    private long fps;
    private long lastTime;
    private String permTitle = "";

    public FPS(String title) {
        setTitle(title);
    }

    public int update() {
        int del = getDelta();
        updateFPS();
        return del;
    }

    public void init() {
        init(DEFAULT_TIME_UNIT);
    }

    public void init(TimeUnit divis) {
        getDelta(divis);
        this.lastFPS = getTime(divis);
    }

    /**
     * Calculate how many milliseconds have passed since last frame.
     * 
     * @param index
     *            - index of the FPS counter
     * 
     * @return milliseconds passed since last frame
     */
    public int getDelta() {
        return getDelta(DEFAULT_TIME_UNIT);
    }

    /**
     * Calculate how many divisions have passed since last frame.
     * 
     * @param index
     *            - index of the FPS counter
     * @param divis
     *            - the division to return
     * 
     * @return divisions passed since last frame
     */
    public int getDelta(TimeUnit divis) {
        long time = getTime(divis);
        int delta = (int) (time - this.lastTime);
        this.lastTime = time;

        return delta;
    }

    public long getTime(TimeUnit divis) {
        long secondConversionRate = divis.convert(1, TimeUnit.SECONDS);
        return (long) (GLFW.glfwGetTime() * secondConversionRate);
    }

    /**
     * Get the accurate system time
     * 
     * @param index
     * 
     * @return The system time in milliseconds
     */
    public long getTime() {
        return getTime(DEFAULT_TIME_UNIT);
    }

    /**
     * Calculate the FPS and set it in the title bar
     */
    private void updateFPS() {
        if (getTime() - this.lastFPS > 1000) {
            if (this.window != 0) {
                GLFW.glfwSetWindowTitle(this.window, getFPSTitle());
            }
            this.fps = 0;
            this.lastFPS += 1000;
        }
        this.fps++;
    }

    private String getFPSTitle() {
        return this.permTitle + " FPS: " + this.fps;
    }

    public void setTitle(String reqTitle) {
        this.permTitle = reqTitle;
    }

    public void enable(long window) {
        this.window = window;
        GLFW.glfwSetWindowTitle(this.window, getFPSTitle());
    }

    public void disable() {
        GLFW.glfwSetWindowTitle(this.window, this.permTitle);
        this.window = 0;
    }

}
