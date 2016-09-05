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
package com.techshroom.emergencylanding.library.lwjgl.control;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.lwjgl.render.VBAO;
import com.techshroom.emergencylanding.library.lwjgl.render.VertexData;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;
import com.techshroom.emergencylanding.library.util.DrawableUtils;

public final class MouseHelp {

    private static class FakeCursor {

        private final double scaleX;
        private final double scaleY;
        VBAO display = null;

        public FakeCursor(ELTexture image, int hotx, int hoty) {
            this.display = Shapes.getQuad(new VertexData().setXYZ(-hotx, -hoty, 0),
                    new VertexData().setXYZ(image.getWidth(), image.getHeight(), 0), Shapes.XY);
            this.display.setTexture(image);
            this.display.setStatic(false);
            this.scaleX = 1;
            this.scaleY = 1;
        }

        public FakeCursor(ELTexture image, int hotx, int hoty, int width, int height) {
            this.display = Shapes.getQuad(new VertexData().setXYZ(-hotx, -hoty, 0),
                    new VertexData().setXYZ(image.getWidth(), image.getHeight(), 0), Shapes.XY);
            this.display.setTexture(image);
            this.display.setStatic(false);
            this.scaleX = image.getWidth() / (double) width;
            this.scaleY = image.getHeight() / (double) height;
        }

        public void drawAt(double x, double y) {
            DrawableUtils.glBeginScale(this.scaleX, this.scaleY, 1);
            DrawableUtils.glBeginTrans(x, y, 0);
            this.display.draw();
            DrawableUtils.glEndTrans();
            DrawableUtils.glEndScale();
        }

    }

    private static final Map<Long, MouseHelp> helperMap = new HashMap<>();

    public static MouseHelp getHelper(long window) {
        MouseHelp help = helperMap.computeIfAbsent(window, MouseHelp::new);
        return help;
    }

    private final long window;
    // private final GLFWMouseButtonCallback thisMBCB =
    // GLFWMouseButtonCallback.create(this);
    // private final GLFWCursorEnterCallback thisCECB =
    // GLFWCursorEnterCallback.create(this);
    private final GLFWCursorPosCallback thisCPCB = GLFWCursorPosCallback.create((window, xpos, ypos) -> {
        ypos = DrawableUtils.getWindowHeight() - ypos;
        double oldX = this.x;
        double oldY = this.y;
        this.x = xpos;
        this.y = ypos;
    });
    @SuppressWarnings("unused")
    // Used to keep a strong ref to the images, etc.
    private Cursor currentCursor;
    private FakeCursor fake;
    private double x;
    private double y;

    private MouseHelp(long window) {
        this.window = window;
        // GLFW.glfwSetMouseButtonCallback(window, this.thisMBCB);
        // GLFW.glfwSetCursorEnterCallback(window, this.thisCECB);
        GLFW.glfwSetCursorPosCallback(window, this.thisCPCB);
        useCursor(Cursor.getStandardCursor(GLFW.GLFW_ARROW_CURSOR));
    }

    private void useCursor(Cursor cursor) {
        (this.currentCursor = cursor).useOnWindow(this.window);
    }

    public void replaceCursor(ELTexture texture, int hotx, int hoty) {
        useCursor(Cursor.createCursor(texture, hotx, hoty));
    }

    /**
     * createFollowCursor is used when you want to hide the mouse cursor while
     * in your window, and use your own that will always stay inside the window,
     * active.
     * 
     * @param texture
     *            - a
     *            {@link com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture
     *            Texture} for the mouse
     * @param hotx
     *            - the hotspotX of the mouse (where the user will click
     *            relative to the top left corner)
     * @param hoty
     *            - the hotspotY of the mouse (where the user will click
     *            relative to the top left corner)
     */
    public void createFollowCursor(ELTexture texture, int hotx, int hoty) {
        replaceCursor(ELTexture.invisible, 0, 0);
        this.fake = new FakeCursor(texture, hotx, hoty);
    }

    /**
     * createFollowCursor is used when you want to hide the mouse cursor while
     * in your window, and use your own that will always stay inside the window,
     * active.
     * 
     * @param texture
     *            - a
     *            {@link com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture
     *            Texture} for the mouse
     * @param width
     *            - the wanted width of the cursor
     * 
     * @param height
     *            - the wanted height of the cursor
     * 
     * @param hotx
     *            - the hotspotX of the mouse (where the user will click
     *            relative to the top left corner)
     * @param hoty
     *            - the hotspotY of the mouse (where the user will click
     *            relative to the top left corner)
     */
    public void createFollowCursor(ELTexture texture, int width, int height, int hotx, int hoty) {
        replaceCursor(ELTexture.invisible, 0, 0);
        this.fake = new FakeCursor(texture, hotx, hoty, width, height);
    }

    public void resetCursor() {
        this.fake = null;
        useCursor(Cursor.getStandardCursor(GLFW.GLFW_ARROW_CURSOR));
    }

    public void onDisplayUpdate() {
        if (this.fake != null) {
            this.fake.drawAt(this.x, this.y);
        }
    }
}
