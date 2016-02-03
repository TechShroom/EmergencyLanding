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
package com.techshroom.emergencylanding.library.lwjgl;

import java.lang.instrument.IllegalClassFormatException;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL;

import com.flowpowered.math.vector.Vector3f;
import com.techshroom.emergencylanding.exst.mods.Mods;
import com.techshroom.emergencylanding.imported.Sync;
import com.techshroom.emergencylanding.library.debug.FPS;
import com.techshroom.emergencylanding.library.lwjgl.control.Keys;
import com.techshroom.emergencylanding.library.lwjgl.control.MouseHelp;
import com.techshroom.emergencylanding.library.lwjgl.render.GLData;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;
import com.techshroom.emergencylanding.library.main.KMain;
import com.techshroom.emergencylanding.library.util.LUtils;
import com.techshroom.emergencylanding.library.util.StackTraceInfo;

public class DisplayLayer {

    static {
        LUtils.init();
    }

    private static final Map<Long, DisplayLayer> createdMap = new HashMap<>();
    public static final Map<Long, DisplayLayer> CREATED =
            Collections.unmodifiableMap(createdMap);

    private final long window;
    private final FPS displayFPSTracker;
    private final Sync synchronizer = new Sync();
    private final GLFWFramebufferSizeCallback sizeCallback;
    private final MouseHelp mouseHelp;
    private final Keys keys;

    /**
     * Initializes the display and KMain instance. Parameter notes are found on
     * the longest argument version.
     * 
     * @param fullscreen
     *            - is fullscreen on at start?
     * @param width
     *            - initial width of screen
     * @param height
     *            - initial height of screen
     * @param title
     *            - title of screen
     * @param resizable
     *            - is the screen resizeable?
     * @param args
     *            - main() args
     * @throws Exception
     *             any exceptions will be thrown
     */
    public static DisplayLayer initDisplay(long fullscreenMonitor, int width,
            int height, String title, boolean resizable, String[] args)
                    throws Exception {
        return DisplayLayer.initDisplay(fullscreenMonitor, width, height, title,
                resizable, true, args);
    }

    /**
     * Initializes the display and KMain instance. Parameter notes are found on
     * the longest argument version.
     * 
     * @param fullscreen
     *            - is fullscreen on at start?
     * @param width
     *            - initial width of screen
     * @param height
     *            - initial height of screen
     * @param title
     *            - title of screen
     * @param resizable
     *            - is the screen resizeable?
     * @param args
     *            - main() args
     * @param vsync
     *            - overrides default vsync option, true
     * @throws Exception
     *             any exceptions will be thrown
     */
    public static DisplayLayer initDisplay(long fullscreenMonitor, int width,
            int height, String title, boolean resizable, boolean vsync,
            String[] args) throws Exception {
        try {
            return DisplayLayer.initDisplay(fullscreenMonitor, width, height,
                    title, resizable, vsync, args,
                    Class.forName(LUtils
                            .getFirstEntryNotThis(DisplayLayer.class.getName()))
                            .asSubclass(KMain.class));
        } catch (ClassCastException cce) {
            if (cce.getStackTrace()[StackTraceInfo.CLIENT_CODE_STACK_INDEX]
                    .getClassName().equals(DisplayLayer.class.getName())) {
                throw new IllegalClassFormatException("Class "
                        + Class.forName(StackTraceInfo.getInvokingClassName())
                        + " not implementing KMain!");
            } else {
                throw cce;
            }
        }
    }

    /**
     * Initializes the display and KMain instance. Parameter notes are found on
     * the longest argument version.
     * 
     * @param fullscreen
     *            - is fullscreen on at start?
     * @param width
     *            - initial width of screen
     * @param height
     *            - initial height of screen
     * @param title
     *            - title of screen
     * @param resizable
     *            - is the screen resizeable?
     * @param args
     *            - main() args
     * @param vsync
     *            - is vsync enabled?
     * @param cls
     *            - overrides the default class for KMain, which is the class
     *            that called the method
     * @throws Exception
     *             any exceptions will be thrown
     */

    public static DisplayLayer initDisplay(long fullscreenMonitor, int width,
            int height, String title, boolean resizable, boolean vsync,
            String[] args, Class<? extends KMain> cls) throws Exception {
        KMain main = cls.newInstance();
        return DisplayLayer.initDisplay(fullscreenMonitor, width, height, title,
                resizable, vsync, args, main);
    }

    public static DisplayLayer initDisplay(long fullscreenMonitor, int width,
            int height, String title, boolean resizable, boolean vsync,
            String[] args, KMain main) throws Exception {
        return new DisplayLayer(fullscreenMonitor, width, height, title,
                resizable, vsync, args, main);
    }

    private DisplayLayer(long fullscreenMonitor, int width, int height,
            String title, boolean resizable, boolean vsync, String[] args,
            KMain main) {
        LUtils.print("Using LWJGL v" + org.lwjgl.Version.getVersion());
        if (GLFW.glfwInit() != GLFW.GLFW_TRUE) {
            throw new IllegalStateException("glfwInit failed!");
        }
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,
                resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE,
                GLFW.GLFW_OPENGL_CORE_PROFILE);
        this.window = GLFW.glfwCreateWindow(width, height, title,
                fullscreenMonitor, 0);
        if (this.window == 0) {
            // uh oh.
            throw new IllegalStateException("window creation failed.");
        }

        GLFW.glfwMakeContextCurrent(this.window);
        GL.createCapabilities(true);

        createdMap.put(this.window, this);

        GLFW.glfwSwapInterval(vsync ? 1 : 0);

        this.sizeCallback = GLFWFramebufferSizeCallback
                .create((win, w, h) -> GLData.resizedRefresh(win));
        GLFW.glfwSetFramebufferSizeCallback(this.window, this.sizeCallback);
        this.mouseHelp = MouseHelp.getHelper(this.window);
        this.keys = Keys.getHelper(this.window);

        String currentMethodName = StackTraceInfo.getCurrentMethodName();
        GLData.notifyOnGLError(currentMethodName);
        KMain.setDisplayThread(Thread.currentThread());
        GLData.notifyOnGLError(currentMethodName);
        KMain.setInst(main);
        GLData.notifyOnGLError(currentMethodName);
        Mods.findAndLoad();
        GLData.notifyOnGLError(currentMethodName);
        GLData.initOpenGL(this.window);
        this.displayFPSTracker = new FPS(title);
        GLData.notifyOnGLError(currentMethodName);
        main.init(this, args);
        GLData.notifyOnGLError(currentMethodName);
        LUtils.print("Using OpenGL v" + LUtils.getGLVer());
    }

    public void loop(int dfps) {
        this.synchronizer.sync(dfps);
        int delta = this.displayFPSTracker.update();
        GLData.clearAndLoad();
        ELTexture.doBindings();
        KMain.getInst().onDisplayUpdate(delta);
        GLData.notifyOnGLError("postImplementationDisplayUpdate");
        this.mouseHelp.onDisplayUpdate();
        GLData.unload();
        GLFW.glfwSwapBuffers(this.window);
        GLFW.glfwPollEvents();
    }

    public void intoFull() {
        throw new UnsupportedOperationException(
                "waiting on @kenzierocks to implement");
    }

    public void outOfFull() {
        throw new UnsupportedOperationException(
                "waiting on @kenzierocks to implement");
    }

    public void destroy() {
        GLFW.glfwDestroyWindow(this.window);
    }

    public void toggleFull() {
        // monitor == fullscreen
        if (GLFW.glfwGetWindowMonitor(this.window) != 0) {
            outOfFull();
        } else {
            intoFull();
        }
    }

    public FPS getDisplayFPSTracker() {
        return this.displayFPSTracker;
    }

    public MouseHelp getMouseHelp() {
        return this.mouseHelp;
    }

    public Keys getKeys() {
        return this.keys;
    }

    public long getWindow() {
        return this.window;
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(this.window) == GLFW.GLFW_TRUE;
    }

    public Vector3f convertFromWindowToFramebuffer(Vector3f vec) {
        IntBuffer widthHolder = BufferUtils.createIntBuffer(2);
        IntBuffer heightHolder = BufferUtils.createIntBuffer(2);
        GLFW.glfwGetWindowSize(this.window, widthHolder, heightHolder);
        widthHolder.position(1);
        heightHolder.position(1);
        GLFW.glfwGetFramebufferSize(this.window, widthHolder, heightHolder);
        return vec.mul(widthHolder.get(1) / (float) widthHolder.get(0),
                heightHolder.get(1) / (float) heightHolder.get(0), 1);
    }

    public static void finalExitCall() {
        LUtils.print(LUtils.LIB_NAME + " is shutting down...");
    }

}
