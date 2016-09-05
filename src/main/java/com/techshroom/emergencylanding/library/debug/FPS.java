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
package com.techshroom.emergencylanding.library.debug;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.lwjgl.glfw.GLFW;

import com.techshroom.emergencylanding.library.util.LUtils;

public final class FPS {

    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;

    static {
        LUtils.init(); // init if not done already, otherwise errors
    }

    private final LinkedList<Long> stored = new LinkedList<>();
    private final TimeUnit divis;
    private final transient long secondConversionRate;
    private long window;
    private String permTitle = "";

    public FPS(String title) {
        this(title, DEFAULT_TIME_UNIT);
    }

    public FPS(String title, TimeUnit divis) {
        setTitle(title);
        this.divis = divis;
        this.secondConversionRate = divis.convert(1, TimeUnit.SECONDS);
    }

    public int update() {
        int del = getDelta();
        updateFPS();
        return del;
    }

    /**
     * Calculate how many milliseconds have passed since last frame.
     * 
     * @return milliseconds passed since last frame
     */
    public int getDelta() {
        long time = getTime();
        if (this.stored.isEmpty()) {
            this.stored.addLast(time);
        }
        int delta = (int) (time - this.stored.getLast());
        this.stored.addLast(time);

        return delta;
    }

    /**
     * Get the accurate system time.
     * 
     * @return The system time in milliseconds
     */
    public long getTime() {
        return (long) (GLFW.glfwGetTime() * this.secondConversionRate);
    }

    /**
     * Calculate the FPS and set it in the title bar
     */
    private void updateFPS() {
        // pop off > one second
        int oldFps = this.stored.size();
        long min = this.stored.getLast() - this.divis.convert(1, TimeUnit.SECONDS);
        while (this.stored.size() > 1 && this.stored.getFirst() < min) {
            this.stored.pollFirst();
        }
        int fps = this.stored.size();
        if (fps != oldFps) {
            if (this.window != 0) {
                GLFW.glfwSetWindowTitle(this.window, getFPSTitle());
            }
        }
    }

    private String getFPSTitle() {
        return this.permTitle + " FPS: " + this.stored.size();
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
