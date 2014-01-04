package emergencylanding.k.library.lwjgl;

import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;
import java.lang.instrument.IllegalClassFormatException;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import de.matthiasmann.twl.renderer.Renderer;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import emergencylanding.k.exst.mods.Mods;
import emergencylanding.k.library.debug.FPS;
import emergencylanding.k.library.lwjgl.control.Keys;
import emergencylanding.k.library.lwjgl.control.MouseHelp;
import emergencylanding.k.library.lwjgl.render.GLData;
import emergencylanding.k.library.lwjgl.tex.ELTexture;
import emergencylanding.k.library.main.KMain;
import emergencylanding.k.library.util.LUtils;
import emergencylanding.k.library.util.StackTraceInfo;

public class DisplayLayer {

    public static String VERSION = "1.1";
    public static String PLATFORM_NAME = "unknown";
    static {
        PLATFORM_NAME = LWJGLUtil.getPlatformName();
        String osName = System.getProperty("os.name");
        if (osName.startsWith("SunOS")) {
            PLATFORM_NAME = "solaris";
        }
    }
    private static String reqTitle = "";
    private static boolean wasResizable;
    private static LWJGLRenderer renderer;

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
    public static void initDisplay(boolean fullscreen, int width, int height,
            String title, boolean resizable, String[] args) throws Exception {
        DisplayLayer.initDisplay(fullscreen, width, height, title, resizable,
                true, args);
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
    public static void initDisplay(boolean fullscreen, int width, int height,
            String title, boolean resizable, boolean vsync, String[] args)
            throws Exception {
        try {
            DisplayLayer.initDisplay(
                    fullscreen,
                    width,
                    height,
                    title,
                    resizable,
                    vsync,
                    args,
                    Class.forName(
                            LUtils.getFirstEntryNotThis(DisplayLayer.class
                                    .getName())).asSubclass(KMain.class));
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

    public static void initDisplay(boolean fullscreen, int width, int height,
            String title, boolean resizable, boolean vsync, String[] args,
            Class<? extends KMain> cls) throws Exception {
        KMain main = cls.newInstance();
        DisplayLayer.initDisplay(fullscreen, width, height, title, resizable,
                vsync, args, main);
    }

    public static void initDisplay(boolean fullscreen, int width, int height,
            String title, boolean resizable, boolean vsync, String[] args,
            KMain main) throws Exception {
        System.err.println(LUtils.TOP_LEVEL.getAbsolutePath());
        System.setProperty("org.lwjgl.librarypath",
                LUtils.TOP_LEVEL.getAbsolutePath() + File.separator + "libs"
                        + File.separator + "natives" + File.separator
                        + PLATFORM_NAME);
        LUtils.print("Using LWJGL v" + Sys.getVersion());
        DisplayMode dm = LUtils.getDisplayMode(width, height, fullscreen);
        if (!dm.isFullscreenCapable() && fullscreen) {
            LUtils.print("Warning! Fullscreen is not supported with width "
                    + width + " and height " + height);
            fullscreen = false;
        }
        DisplayLayer.reqTitle = title.toString();
        Display.setDisplayMode(dm);
        if (!fullscreen) {
            Display.setTitle(DisplayLayer.reqTitle);
        }
        PixelFormat pixelFormat = new PixelFormat();
        ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
                .withForwardCompatible(true).withProfileCore(true);
        System.err.println("Using contexAttributes " + contextAtrributes);
        Display.create(pixelFormat, contextAtrributes);
        Display.setFullscreen(fullscreen);
        Display.setResizable(resizable && !fullscreen);
        Display.setVSyncEnabled(vsync);
        GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        KMain.setDisplayThread(Thread.currentThread());
        GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        KMain.setInst(main);
        GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        Mods.findAndLoad();
        GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        GLData.initOpenGL();
        FPS.init(0);
        GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        FPS.setTitle(DisplayLayer.reqTitle);
        GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        main.init(args);
        GLData.notifyOnGLError(StackTraceInfo.getCurrentMethodName());
        LUtils.print("Using OpenGL v" + LUtils.getGLVer());
    }

    public static void loop(int dfps) throws LWJGLException {
        Display.sync(dfps);
        int delta = FPS.update(0);
        if (Display.wasResized()) {
            GLData.resizedRefresh();
        }
        GLData.clearAndLoad();
        ELTexture.doBindings();
        KMain.getInst().onDisplayUpdate(delta);
        MouseHelp.onDisplayUpdate();
        GLData.unload();
        Display.update(false);
        OrgLWJGLOpenGLPackageAccess.updateImplementation();
    }

    public static void readDevices() {
        OrgLWJGLOpenGLPackageAccess.pollDevices();
        Keys.read();
        MouseHelp.read();
    }

    public static void intoFull() throws LWJGLException {
        Display.setFullscreen(true);
        DisplayLayer.wasResizable = Display.isResizable();
        Display.setResizable(false);
    }

    public static void outOfFull() throws LWJGLException {
        Display.setResizable(DisplayLayer.wasResizable);
        Display.setFullscreen(false);
    }

    public static void destroy() {
        Display.destroy();
        Frame[] frms = Frame.getFrames();
        for (Frame frm : frms) {
            if (frm.isVisible()) {
                frm.setVisible(false);
                frm.dispose();
                System.err
                        .println("CrashCourse has closed a JFrame called "
                                + frm.getTitle()
                                + ", which would have stalled the application's closing state. Please fix this!");
            }
        }
    }

    public static void toggleFull() {
        try {
            if (Display.isFullscreen()) {
                DisplayLayer.outOfFull();
            } else {
                DisplayLayer.intoFull();
            }
        } catch (LWJGLException e) {
        }
    }

    /**
     * @param width
     * @param height
     * @param fullscreen
     * @return if the aspect ratio matches with the current screen
     * @deprecated Not useful anymore, calls should go to
     *             {@link DisplayMode#isFullscreenCapable()}
     */
    @Deprecated
    public static boolean compatibleWithFullscreen(int width, int height,
            boolean fullscreen) {
        int dw, dh;
        dw = Toolkit.getDefaultToolkit().getScreenSize().width;
        dh = Toolkit.getDefaultToolkit().getScreenSize().height;
        float aspect_requested = (float) width / (float) height;
        float aspect_required = (float) dw / (float) dh;
        return aspect_requested == aspect_required;
    }

    public static Renderer getLWJGLRenderer() {
        try {
            return DisplayLayer.renderer != null ? DisplayLayer.renderer
                    : (DisplayLayer.renderer = new LWJGLRenderer());
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
