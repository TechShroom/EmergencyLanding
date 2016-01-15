package emergencylanding.k.library.debug;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import emergencylanding.k.library.main.KMain;
import emergencylanding.k.library.util.LUtils;

public class FPS {
    static {
        LUtils.init(); // init if not done already, otherwise errors
    }
    
    public static final long millis = 1000, micro = 1000 * FPS.millis;
    public static final int MAX_FPS_COUNTERS = 100;
    private static int nextIndex = 0;
    public static final int DISPLAY_FPS_COUNTER = genIndex();
    /** time at last frame */
    private static long[] lastFrame = new long[FPS.MAX_FPS_COUNTERS];
    /** frames per second */
    private static int[] fps = new int[FPS.MAX_FPS_COUNTERS];
    /** last fps time */
    private static long[] lastFPS = new long[FPS.MAX_FPS_COUNTERS];
    private static String permTitle = "";
    private static boolean[] enabled = new boolean[FPS.MAX_FPS_COUNTERS];
    private static boolean[] hasFPSTitle = new boolean[FPS.MAX_FPS_COUNTERS];
    private static String tempTitle;

    public static int update(int index) {
        FPS.setTitleIfNotSet();
        int del = FPS.getDelta(index);
        FPS.updateFPS(index);
        return del;
    }

    public static void init(int index) {
        FPS.init(index, FPS.millis);
    }

    public static void init(int index, long divis) {
        FPS.getDelta(index, divis);
        FPS.lastFPS[index] = FPS.getTime(divis);
        FPS.permTitle = Display.getTitle();
    }

    /**
     * Calculate how many milliseconds have passed since last frame.
     * 
     * @param index
     *            - index of the FPS counter
     * 
     * @return milliseconds passed since last frame
     */
    public static int getDelta(int index) {
        return FPS.getDelta(index, FPS.millis);
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
    public static int getDelta(int index, long divis) {
        long time = FPS.getTime(divis);
        int delta = (int) (time - FPS.lastFrame[index]);
        FPS.lastFrame[index] = time;

        return delta;
    }

    public static long getTime(long divis) {
        return Sys.getTime() * divis / Sys.getTimerResolution();
    }

    /**
     * Get the accurate system time
     * 
     * @param index
     * 
     * @return The system time in milliseconds
     */
    public static long getTime() {
        return FPS.getTime(FPS.millis);
    }

    /**
     * Calculate the FPS and set it in the title bar
     */
    private static void updateFPS(int index) {
        if (FPS.getTime() - FPS.lastFPS[index] > 1000) {
            if (FPS.enabled[index]) {
                FPS.setTitleOnDisplayThread(FPS.fps[index]);
                FPS.hasFPSTitle[index] = true;
            } else if (FPS.hasFPSTitle[index]) {
                Display.setTitle(FPS.permTitle);
            }
            FPS.fps[index] = 0;
            FPS.lastFPS[index] += 1000;
        }
        FPS.fps[index]++;
    }

    private synchronized static void setTitleOnDisplayThread(int fps) {
        FPS.tempTitle = FPS.permTitle + " FPS: " + fps;
        FPS.setTitleIfNotSet();
    }

    private static void setTitleIfNotSet() {
        if (Thread.currentThread().equals(KMain.getDisplayThread())
                && FPS.tempTitle != null) {
            Display.setTitle(FPS.tempTitle);
            FPS.tempTitle = null;
        }
    }

    public static void setTitle(String reqTitle) {
        FPS.permTitle = reqTitle;
    }

    public static void enable(int index) {
        FPS.enabled[index] = true;
    }

    public static void disable(int index) {
        FPS.enabled[index] = false;
    }

    public static int genIndex() {
        if (FPS.nextIndex > FPS.MAX_FPS_COUNTERS) {
            throw new IndexOutOfBoundsException("Too many FPS counters");
        }
        return FPS.nextIndex++;
    }

}
