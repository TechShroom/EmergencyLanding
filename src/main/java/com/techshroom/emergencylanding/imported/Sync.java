package com.techshroom.emergencylanding.imported;

import org.lwjgl.glfw.GLFW;

/**
 * A highly accurate sync method that continually adapts to the system it runs
 * on to provide reliable results.
 * 
 * Modified to allow instances of different syncs.
 * 
 * @author Riven
 * @author kappaOne
 */
public class Sync {

    /** number of nano seconds in a second */
    private static final long NANOS_IN_SECOND = 1000L * 1000L * 1000L;

    /** The time to sleep/yield until the next frame */
    private long nextFrame = 0;

    /** whether the initialisation code has run */
    private boolean initialised = false;

    /**
     * for calculating the averages the previous sleep/yield times are stored
     */
    private RunningAvg sleepDurations = new RunningAvg(10);
    private RunningAvg yieldDurations = new RunningAvg(10);

    static {
        // Starts Windows hack if needed
        String osName = System.getProperty("os.name");

        // Prevents this class from starting extra thread as
        // org.lwjgl.opengl.Sync usually does this part.
        Thread[] t_s = new Thread[Thread.activeCount()];
        Thread.enumerate(t_s);
        boolean needsAccuracy = true;
        for (Thread t : t_s) {
            if (t == null || t.getName() == null) {
                continue;
            }
            if (t.getName().equals("LWJGL Timer")
                    || t.getName().equals("KCore Win Fix")) {
                needsAccuracy = false;
            }
        }

        if (osName.startsWith("Win") && needsAccuracy) {
            // On windows the sleep functions can be highly inaccurate by
            // over 10ms making in unusable. However it can be forced to
            // be a bit more accurate by running a separate sleeping daemon
            // thread.
            Thread timerAccuracyThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (Exception e) {
                    }
                }
            });

            timerAccuracyThread.setName("LWJGL Timer");
            timerAccuracyThread.setDaemon(true);
            timerAccuracyThread.start();
        }
    }

    /**
     * An accurate sync method that will attempt to run at a constant frame
     * rate. It should be called once every frame.
     * 
     * @param fps
     *            - the desired frame rate, in frames per second
     */
    public void sync(int fps) {
        if (fps <= 0) {
            return;
        }
        if (!this.initialised) {
            initialise();
        }

        try {
            // sleep until the average sleep time is greater than the time
            // remaining till nextFrame
            for (long t0 = getTime(), t1; this.nextFrame
                    - t0 > this.sleepDurations.avg(); t0 = t1) {
                Thread.sleep(1);
                this.sleepDurations.add((t1 = getTime()) - t0); // update
                                                                // average
                // sleep time
            }

            // slowly dampen sleep average if too high to avoid yielding too
            // much
            this.sleepDurations.dampenForLowResTicker();

            // yield until the average yield time is greater than the time
            // remaining till nextFrame
            for (long t0 = getTime(), t1; this.nextFrame
                    - t0 > this.yieldDurations.avg(); t0 = t1) {
                Thread.yield();
                this.yieldDurations.add((t1 = getTime()) - t0); // update
                                                                // average
                // yield time
            }
        } catch (InterruptedException e) {

        }

        // schedule next frame, drop frame(s) if already too late for next frame
        this.nextFrame = Math.max(this.nextFrame + Sync.NANOS_IN_SECOND / fps,
                getTime());
    }

    /**
     * This method will initialise the sync method by setting initial values for
     * sleepDurations/yieldDurations and nextFrame.
     * 
     * 
     */
    private void initialise() {
        this.initialised = true;

        this.sleepDurations.init(1000 * 1000);
        this.yieldDurations.init((int) (-(getTime() - getTime()) * 1.333));

        this.nextFrame = getTime();
    }

    /**
     * Get the system time in nano seconds
     * 
     * @return will return the current time in nano's
     */
    private long getTime() {
        return (long) (GLFW.glfwGetTime() * Sync.NANOS_IN_SECOND);
    }

    public static class RunningAvg {

        private final long[] slots;
        private int offset;

        private static final long DAMPEN_THRESHOLD = 10 * 1000L * 1000L; // 10ms
        private static final float DAMPEN_FACTOR = 0.9f; // don't change: 0.9f

        // is exactly right!

        public RunningAvg(int slotCount) {
            this.slots = new long[slotCount];
            this.offset = 0;
        }

        public void init(long value) {
            while (this.offset < this.slots.length) {
                this.slots[this.offset++] = value;
            }
        }

        public void add(long value) {
            this.slots[this.offset++ % this.slots.length] = value;
            this.offset %= this.slots.length;
        }

        public long avg() {
            long sum = 0;
            for (int i = 0; i < this.slots.length; i++) {
                sum += this.slots[i];
            }
            return sum / this.slots.length;
        }

        public void dampenForLowResTicker() {
            if (avg() > RunningAvg.DAMPEN_THRESHOLD) {
                for (int i = 0; i < this.slots.length; i++) {
                    this.slots[i] *= RunningAvg.DAMPEN_FACTOR;
                }
            }
        }
    }
}