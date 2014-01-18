package emergencylanding.k.library.lwjgl.control;

import emergencylanding.k.library.internalstate.Victor;
import emergencylanding.k.library.lwjgl.Shapes;
import emergencylanding.k.library.lwjgl.render.VBAO;
import emergencylanding.k.library.lwjgl.render.VertexData;
import emergencylanding.k.library.lwjgl.tex.BufferedTexture;
import emergencylanding.k.library.lwjgl.tex.ELTexture;
import emergencylanding.k.library.util.DrawableUtils;
import emergencylanding.k.library.util.LUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

public class MouseHelp {
    private static class FakeCursor {
        VBAO display = null;

        public FakeCursor(ELTexture image, int hotx, int hoty) {
            display = Shapes.getQuad(new VertexData().setXYZ(-hotx, -hoty, 0),
                    new VertexData().setXYZ(image.getWidth(),
                            image.getHeight(), 0), Shapes.XY);
            display.setTexture(image);
        }

        public void drawAt(int x, int y) {
            display.setXYZOff(new Victor(x, y, 0));
            display.draw();
        }

    }

    public static final int LMB = 0, RMB = 1, ANY = -1;

    private static MouseHelp inst = new MouseHelp();
    private int dx = 0;
    private int dy = 0;
    private int x = 0;
    private int y = 0;
    private FakeCursor fake;
    public static int buttons;
    private boolean[] thisFrameClick = new boolean[MouseHelp.buttons];
    private boolean[] lastFrameClick = new boolean[MouseHelp.buttons];
    private static Cursor nativec = null;

    private MouseHelp() {
        try {
            Mouse.create();
            MouseHelp.nativec = Mouse.getNativeCursor();
            Mouse.setClipMouseCoordinatesToWindow(false);
            MouseHelp.inst = this;
            MouseHelp.buttons = Mouse.getButtonCount();
            thisFrameClick = new boolean[MouseHelp.buttons];
            lastFrameClick = new boolean[MouseHelp.buttons];
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void read() {
        try {
            MouseHelp.inst.dx = Mouse.getDX();
            MouseHelp.inst.dy = Mouse.getDY();
            MouseHelp.inst.x = Mouse.getX();
            MouseHelp.inst.y = Mouse.getY();
            for (int i = 0; i < MouseHelp.buttons; i++) {
                MouseHelp.inst.lastFrameClick[i] = MouseHelp.inst.thisFrameClick[i];
                MouseHelp.inst.thisFrameClick[i] = Mouse.isButtonDown(i);
            }
        } catch (Exception e) {
        }
    }

    private void onValueChanges() {
        if (fake != null) {
            fake.drawAt(x, y);
        }
    }

    public static void replaceCursor(BufferedImage image, int hotx, int hoty) {
        int length = image.getWidth() * image.getHeight();
        IntBuffer imgbuf = BufferUtils.createIntBuffer(length);
        int ih = image.getHeight();
        int iw = image.getWidth();
        for (int i = 0; i < length; i++) {
            int x = i % iw;
            int y = i / ih;
            int rev_y = image.getHeight() - y - 1;
            imgbuf.put(image.getRGB(x, rev_y));
        }
        imgbuf.rewind();
        try {
            Cursor c = new Cursor(image.getWidth(), image.getHeight(), hotx,
                    hoty, 1, imgbuf, null);
            Mouse.setNativeCursor(c);
        } catch (LWJGLException e) {
            e.printStackTrace();
            LUtils.print("Mouse cursor couldn't be created/implemented!");
        }
    }

    public static void replaceCursor(ELTexture texture, int hotx, int hoty) {
        MouseHelp.replaceCursor(texture.toBufferedImage(), hotx, hoty);
    }

    /**
     * createFollowCursor is used when you want to hide the mouse cursor while
     * in your window, and use your own that will always stay inside the window,
     * active.
     * 
     * @param image
     *            - a {@link java.awt.image.BufferedImage} for the mouse
     * @param hotx
     *            - the hotspotX of the mouse (where the user will click
     *            relative to the top left corner)
     * @param hoty
     *            - the hotspotY of the mouse (where the user will click
     *            relative to the top left corner)
     */
    public static void createFollowCursor(BufferedImage image, int hotx,
            int hoty) {
        MouseHelp.createFollowCursor(new BufferedTexture(image), hotx, hoty);
    }

    /**
     * createFollowCursor is used when you want to hide the mouse cursor while
     * in your window, and use your own that will always stay inside the window,
     * active.
     * 
     * @param texture
     *            - a {@link emergencylanding.k.library.lwjgl.tex.ELTexture
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
    public static void createFollowCursor(ELTexture texture, int hotx, int hoty) {
        /*
         * Hide mouse cursor in our window (prevents it from derping like
         * Synthesia)
         */
        MouseHelp.replaceCursor(ELTexture.invisible, 0, 0);
        MouseHelp.inst.fake = new FakeCursor(texture, hotx, hoty);
    }

    /**
     * createFollowCursor is used when you want to hide the mouse cursor while
     * in your window, and use your own that will always stay inside the window,
     * active.
     * 
     * @param texture
     *            - a {@link emergencylanding.k.library.lwjgl.tex.ELTexture
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
    public static void createFollowCursor(ELTexture texture, int width,
            int height, int hotx, int hoty) {
        /*
         * Hide mouse cursor in our window (prevents it from derping like
         * Synthesia)
         */
        MouseHelp.replaceCursor(ELTexture.invisible, 0, 0);
        texture = new BufferedTexture(DrawableUtils.scaledBufferedImage(
                texture.toBufferedImage(), width, height));
        MouseHelp.inst.fake = new FakeCursor(texture, hotx, hoty);
    }

    public static void resetCursor() {
        try {
            Mouse.setNativeCursor(MouseHelp.nativec);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static int getDX() {
        return MouseHelp.inst.dx;
    }

    public static int getDY() {
        return MouseHelp.inst.dy;
    }

    public static int getX() {
        return MouseHelp.inst.x;
    }

    public static int getY() {
        return MouseHelp.inst.y;
    }

    public static boolean isButtonDown(int buttonID) {
        if (buttonID == ANY) {
            boolean res = false;
            for (boolean b : inst.thisFrameClick) {
                res = b || res;
            }
            return res;
        }
        return MouseHelp.inst.thisFrameClick[buttonID];
    }

    public static boolean wasButtonClicked(int buttonID) {
        if (buttonID == ANY) {
            boolean res = false;
            int in = 0;
            for (boolean b : inst.lastFrameClick) {
                res = (b && !MouseHelp.isButtonDown(in)) || res;
                in++;
            }
            return res;
        }
        return MouseHelp.inst.lastFrameClick[buttonID]
                && !MouseHelp.isButtonDown(buttonID);
    }

    public static void onDisplayUpdate() {
        MouseHelp.inst.onValueChanges();
    }

    public static boolean clickedInRect(Rectangle check, int buttonToCheck) {
        return check.contains(MouseHelp.getX(), MouseHelp.getY())
                && MouseHelp.wasButtonClicked(buttonToCheck);
    }

    public static boolean clickingInRect(Rectangle check, int buttonToCheck) {
        return check.contains(MouseHelp.getX(), MouseHelp.getY())
                && MouseHelp.isButtonDown(buttonToCheck);
    }
}
