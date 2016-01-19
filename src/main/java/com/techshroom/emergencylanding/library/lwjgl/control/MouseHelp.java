package com.techshroom.emergencylanding.library.lwjgl.control;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.lwjgl.render.VBAO;
import com.techshroom.emergencylanding.library.lwjgl.render.VertexData;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;
import com.techshroom.emergencylanding.library.util.DrawableUtils;

public final class MouseHelp implements GLFWMouseButtonCallback.SAM,
        GLFWCursorEnterCallback.SAM, GLFWCursorPosCallback.SAM {

    private static class FakeCursor {

        private final double scaleX;
        private final double scaleY;
        VBAO display = null;

        public FakeCursor(ELTexture image, int hotx, int hoty) {
            this.display = Shapes.getQuad(
                    new VertexData().setXYZ(-hotx, -hoty, 0), new VertexData()
                            .setXYZ(image.getWidth(), image.getHeight(), 0),
                    Shapes.XY);
            this.display.setTexture(image);
            this.display.setStatic(false);
            this.scaleX = 1;
            this.scaleY = 1;
        }

        public FakeCursor(ELTexture image, int hotx, int hoty, int width,
                int height) {
            this.display = Shapes.getQuad(
                    new VertexData().setXYZ(-hotx, -hoty, 0), new VertexData()
                            .setXYZ(image.getWidth(), image.getHeight(), 0),
                    Shapes.XY);
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
    private final GLFWMouseButtonCallback thisMBCB =
            GLFWMouseButtonCallback.create(this);
    private final GLFWCursorEnterCallback thisCECB =
            GLFWCursorEnterCallback.create(this);
    private final GLFWCursorPosCallback thisCPCB =
            GLFWCursorPosCallback.create(this);
    @SuppressWarnings("unused")
    // Used to keep a strong ref to the images, etc.
    private Cursor currentCursor;
    private FakeCursor fake;
    private double x;
    private double y;

    private MouseHelp(long window) {
        this.window = window;
        GLFW.glfwSetMouseButtonCallback(window, this.thisMBCB);
        GLFW.glfwSetCursorEnterCallback(window, this.thisCECB);
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
     * @see {@link MouseHelp#createFollowCursor(BufferedImage, int, int)
     *      createFollowCursor(BufferedImage, int, int)}
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
     * @see {@link MouseHelp#createFollowCursor(BufferedImage, int, int)
     *      createFollowCursor(BufferedImage, int, int)}
     */
    public void createFollowCursor(ELTexture texture, int width, int height,
            int hotx, int hoty) {
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

    @Override
    public void invoke(long window, int button, int action, int mods) {
    }

    @Override
    public void invoke(long window, double xpos, double ypos) {
        ypos = DrawableUtils.getWindowHeight() - ypos;
        double oldX = this.x;
        double oldY = this.y;
        this.x = xpos;
        this.y = ypos;
    }

    @Override
    public void invoke(long window, int entered) {

    }
}
