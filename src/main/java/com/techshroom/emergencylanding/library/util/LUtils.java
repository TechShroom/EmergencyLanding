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
package com.techshroom.emergencylanding.library.util;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.Platform;

import com.google.common.io.ByteStreams;
import com.techshroom.emergencylanding.library.util.interfaces.IOConsumer;

public final class LUtils {

    /**
     * A dummy method to load this class. Does nothing.
     */
    public static void init() {
    }

    public static String VERSION = "1.3.1";

    public static final String LIB_NAME = "EmergencyLanding".intern();
    public static final String SHORT_LIB_NAME = "EL".intern();
    private static final String LOWER_LIB_NAME =
            LIB_NAME.toLowerCase().intern();
    private static final String LOWER_SHORT_LIB_NAME =
            SHORT_LIB_NAME.toLowerCase().intern();
    private static final GLFWErrorCallback ERROR_CB;

    /**
     * The default system streams, before overload.
     */
    public static PrintStream sysout = System.out, syserr = System.err;
    public static String PLATFORM_NAME = "unknown";

    public static final String elPrintStr =
            String.format("[" + LIB_NAME + "-%s]", LUtils.VERSION);

    public static void print(String msg) {
        System.err.println(elPrintStr + " " + msg);
    }

    /**
     * The top level of the game/tool
     */
    public static String TOP_LEVEL = null;
    static {
        try {
            // reuse KCore's data
            LUtils.TOP_LEVEL = Paths.get(".").toAbsolutePath().toString()
                    .replace(File.separatorChar, '/').replaceFirst("/$", "");
            LUtils.print("Using TOP_LEVEL " + TOP_LEVEL);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * The top level of emergency landing, used to load our shaders.
     */
    private static String EL_TOP = null;
    static {
        String tempName = LUtils.class.getPackage().getName();
        int levels = Strings.count(tempName, '.') + 2;
        tempName = LUtils.class.getResource("LUtils.class").getFile()
                // .replace('/', File.separatorChar)// .substring(1)
                .replace("%20", " ");
        for (int i = 0; i < levels; i++) {
            tempName = tempName.substring(0, tempName.lastIndexOf("/"));
        }
        LUtils.print(tempName);
        if (tempName.endsWith("!")) {
            // jar files: natives are in TOP_LEVEL
            LUtils.print("Assumed JAR launch.");
            EL_TOP = TOP_LEVEL;
        } else {
            EL_TOP = ((tempName.startsWith("/") ? "" : "/") + tempName)
                    .replace("/C:/", "C:/").replace("\\C:\\", "C:\\");
        }
        LUtils.print("Using EL_TOP " + EL_TOP);
    }

    static {
        Configuration.DEBUG.set(true);
        PLATFORM_NAME = Platform.get().getName();
        String osName = System.getProperty("os.name");
        if (osName.startsWith("SunOS")) {
            PLATFORM_NAME = "solaris";
        }
        overrideStandardStreams();
        ERROR_CB = GLFWErrorCallback.createPrint();
        GLFW.glfwSetErrorCallback(ERROR_CB);
    }

    /**
     * Adds the specified path to the java library path
     *
     * @param pathToAdd
     *            the path to add
     * @throws Exception
     */
    public static void addLibraryPath(String pathToAdd) throws Exception {
        final Field usrPathsField =
                ClassLoader.class.getDeclaredField("usr_paths");
        usrPathsField.setAccessible(true);

        // get array of paths
        final String[] paths = (String[]) usrPathsField.get(null);

        // check if the path to add is already present
        for (String path : paths) {
            if (path.equals(pathToAdd)) {
                return;
            }
        }

        // add the new path
        final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
        newPaths[newPaths.length - 1] = pathToAdd;
        usrPathsField.set(null, newPaths);
    }

    private static void overrideStandardStreams() {
        System.err.println("Replacing streams with methodized...");
        MethodizedSTDStream sysout = new MethodizedSTDStream(System.out);
        System.setOut(new PrintStream(sysout));
        MethodizedSTDStream syserr = new MethodizedSTDStream(System.err);
        System.setErr(new PrintStream(syserr));
        syserr.orig.println("Finished.");
    }

    public static final int debugLevel = Integer.parseInt(
            System.getProperty(LOWER_SHORT_LIB_NAME + ".debug.level", "0"));

    static {
        System.err.println(
                LOWER_SHORT_LIB_NAME + ".debug.level" + ": " + debugLevel);
    }

    /**
     * Gets a boolean argument safely
     * 
     * @param args
     *            - the args list from which to retrieve the argument
     * @param index
     *            - the index of the wanted argument
     * @param def
     *            - a default value to fallback on
     * @return the wanted boolean argument value, or the default value
     */
    public static boolean getArgB(String[] args, int index, boolean def) {
        return Boolean.valueOf(
                LUtils.getArgS(args, index, Boolean.valueOf(def).toString()));
    }

    /**
     * Gets a integer argument safely
     * 
     * @param args
     *            - the args list from which to retrieve the argument
     * @param index
     *            - the index of the wanted argument
     * @param def
     *            - a default value to fallback on
     * @return the wanted integer argument value, or the default value
     */
    public static int getArgI(String[] args, int index, int def) {
        return Integer.valueOf(
                LUtils.getArgS(args, index, Integer.valueOf(def).toString()));
    }

    /**
     * Gets a float argument safely
     * 
     * @param args
     *            - the args list from which to retrieve the argument
     * @param index
     *            - the index of the wanted argument
     * @param def
     *            - a default value to fallback on
     * @return the wanted float argument value, or the default value
     */
    public static float getArgF(String[] args, int index, float def) {
        return Float.valueOf(
                LUtils.getArgS(args, index, Float.valueOf(def).toString()));
    }

    /**
     * Gets a double argument safely
     * 
     * @param args
     *            - the args list from which to retrieve the argument
     * @param index
     *            - the index of the wanted argument
     * @param def
     *            - a default value to fallback on
     * @return the wanted double argument value, or the default value
     */
    public static double getArgD(String[] args, int index, double def) {
        return Double.valueOf(
                LUtils.getArgS(args, index, Double.valueOf(def).toString()));
    }

    /**
     * Gets a String argument safely
     * 
     * @param args
     *            - the args list from which to retrieve the argument
     * @param index
     *            - the index of the wanted argument
     * @param def
     *            - a default value to fallback on
     * @return the wanted String argument value, or the default value
     */
    public static String getArgS(String[] args, int index, String def) {
        if (args == null) {
            return def;
        }
        return args.length <= index ? def
                : args[index] == null ? def : args[index];
    }

    /**
     * Gets an argument safely
     * 
     * @param src
     *            - the args list from which to retrieve the argument
     * @param index
     *            - the index of the wanted argument
     * @param def
     *            - a default value to fallback on
     * @return the wanted argument value, or the default value
     */
    public static <T> T getArg(T[] src, int index, T def) {
        if (src == null) {
            return def;
        }
        return src.length <= index ? def
                : src[index] == null ? def : src[index];
    }

    /**
     * Checks for the given OpenGL version (eg. 3.0.2)
     * 
     * @param vers
     *            - the wanted version
     * @return true if the actual version is the same as or newer than the
     *         wanted version, false otherwise
     */
    public static boolean isVersionAvaliable(String vers) {
        String cver = getGLVer();
        if (cver.indexOf(' ') > -1) {
            cver = cver.substring(0, cver.indexOf(' '));
        }
        LUtils.print("Comparing " + cver + " to " + vers);
        String[] cver_sep = cver.split("\\.", 3);
        String[] vers_sep = vers.split("\\.", 3);
        int[] cver_sepi = new int[3];
        int[] vers_sepi = new int[3];
        int min = LUtils.minAll(cver_sep.length, vers_sep.length, 3);
        for (int i = 0; i < min; i++) {
            cver_sepi[i] = Integer.parseInt(cver_sep[i]);
            vers_sepi[i] = Integer.parseInt(vers_sep[i]);
        }
        boolean ret = cver_sepi[0] >= vers_sepi[0]
                && cver_sepi[1] >= vers_sepi[1] && cver_sepi[2] >= vers_sepi[2];
        LUtils.print("Returning " + ret);
        return ret;
    }

    /**
     * Gets the smallest of all the given ints
     * 
     * @param ints
     *            - the set of ints to use
     * @return the smallest int from ints
     */
    public static int minAll(int... ints) {
        int min = Integer.MAX_VALUE;
        for (int i : ints) {
            // System.out.println("Comparing " + i + " and " + min);
            min = Math.min(min, i);
        }
        // System.out.println("Result is " + min);
        return min;
    }

    /**
     * Turns a {@link MidiDevice.Info} list into a list of user friendly strings
     * 
     * @param info
     *            - the list of MidiDevice.Infos to use
     * @return a list of Strings representing the given Infos
     */
    public static List<String> getInfoAsString(Info[] info) {
        List<String> out = new ArrayList<String>();
        for (Info i : info) {
            out.add(i + "" + i.getClass().getName());
        }
        return out;
    }

    /**
     * Check for integer
     * 
     * @param test
     *            - the String to check for integer
     * @return if the String represents an integer
     */
    public static boolean isInt(String test) {
        try {
            Integer.parseInt(test);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the current OpenGL version
     * 
     * @return {@link GL11#GL_VERSION}
     */
    public static String getGLVer() {
        return glGetString(GL_VERSION);
    }

    /**
     * Gets the first thing in the stack that is not the given class name
     * 
     * @param name
     *            - a class name
     * @return the class that is not the given class
     */
    public static String getFirstEntryNotThis(String name) {
        String ret = "no class found";
        int level = StackTraceInfo.INVOKING_METHOD_ZERO;
        try {
            while (StackTraceInfo.getCurrentClassName(level).equals(name)) {
                level++;
            }
            ret = StackTraceInfo.getCurrentClassName(level);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Gets an input stream from a path
     * 
     * @param path
     *            - the path, must be absolute
     * @return the input stream, or null if not possible to get an input stream
     * @throws IOException
     *             if there are I/O errors
     */
    public static <R> R processPathData(String path, IOConsumer<R> consumer)
            throws IOException {
        LUtils.print("[Retriving InputStream for '" + path + "']");
        // Normalize to UNIX style
        path = path.replace(File.separatorChar, '/');

        int isType = 0; // undefined=-1;fileis=0;zipis=1;jaris=1
        List<String> pathparts = Arrays.asList(path.split("/"));
        for (String part : pathparts) {
            if (part.endsWith(".zip") || part.endsWith("jar")
                    && !(pathparts.indexOf(part) == pathparts.size() - 1)) {
                if (isType == 1) {
                    isType = 2;
                    break;
                } else {
                    isType = 1;
                    break;
                }
            }
        }

        if (isType == 0) {
            LUtils.print("Using raw file input stream");
            try (InputStream stream = new FileInputStream(path)) {
                return consumer.consumeStream(stream);
            }
        } else if (isType == 1 || isType == 2) {
            LUtils.print("Using recursive zip/jar searcher style " + isType);
            ArrayList<Integer> indexes = new ArrayList<Integer>();
            for (int i = 0; i < pathparts.size(); i++) {
                if (pathparts.get(i).endsWith(".zip")
                        || pathparts.get(i).endsWith(".jar")) {
                    LUtils.print(
                            "Adding zip/jar " + pathparts.get(i) + " at " + i);
                    indexes.add(i);
                }
            }
            String pathToCurrFile = "";
            for (int i = 0; i <= indexes.get(0); i++) {
                String temp_ = pathparts.get(i);
                LUtils.print(String.format("Appending '%s' to '%s'", temp_,
                        pathToCurrFile));
                pathToCurrFile += temp_ + "/";
            }
            String file =
                    pathToCurrFile.substring(0, pathToCurrFile.length() - 1);
            String extra = path.replace(pathToCurrFile, "");
            LUtils.print("Attempting to load from " + file);
            try (ZipFile zf = new ZipFile(file);) {
                ZipEntry ze = zf.getEntry(extra);
                try (InputStream stream = zf.getInputStream(ze)) {
                    return consumer.consumeStream(stream);
                }
            }
        }
        throw new IllegalArgumentException("Inaccessible data " + path);
    }

    public static String getELTop() {
        return EL_TOP;
    }

    public static void setIcon(final InputStream is) {
        throw new UnsupportedOperationException(
                "GLFW does not support setting icons.");
        /*
         * GLFW cannot setIcons at this time if (Platform.get() ==
         * Platform.MACOSX) { // Set in the dock try {
         * Application.getApplication().setDockIconImage(ImageIO.read(is));
         * System.err.println("Using 1 icon"); } catch (IOException e) {
         * e.printStackTrace(); } return; } Runnable r = new Runnable() {
         * 
         * @Override public void run() { ByteBuffer[] icondata =
         * IconLoader.load(is); GLFW.glfic int used = Display.setIcon(icondata);
         * System.err.println("Using " + used + " icon(s)"); } };
         * System.err.println(Thread.currentThread());
         * System.err.println(KMain.getDisplayThread()); if
         * (Thread.currentThread() == KMain.getDisplayThread()) {
         * System.err.println("Early icon load"); r.run(); } else {
         * ELTexture.addRunnableToQueue(r); }
         */
    }

    public static ByteBuffer inputStreamToDirectByteBuffer(
            Supplier<InputStream> streamSupplier) throws IOException {
        try (InputStream stream = streamSupplier.get()) {
            byte[] data = ByteStreams.toByteArray(stream);
            return (ByteBuffer) BufferUtils.createByteBuffer(data.length)
                    .put(data).rewind();
        }
    }

}
