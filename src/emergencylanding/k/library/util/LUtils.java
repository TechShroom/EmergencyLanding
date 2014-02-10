package emergencylanding.k.library.util;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import k.core.util.Helper;
import k.core.util.classes.StackTraceInfo;
import k.core.util.strings.Strings;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import emergencylanding.k.library.lwjgl.DisplayLayer;

public class LUtils {

	/**
	 * A dummy method to load this class. Does nothing.
	 */
	public static void init() {

	}

	/**
	 * What packages are accepted for EL
	 */
	private static final String[] ACCEPT = { "emergencylanding.k.*" };

	/**
	 * The default system streams, before overload.
	 */
	public static PrintStream sysout = System.out, syserr = System.err;
	public static String PLATFORM_NAME = "unknown";

	static {
		/*
		 * If you need to change LWJGsystem properties, do it before this
		 * comment!
		 */
		PLATFORM_NAME = LWJGLUtil.getPlatformName();
		String osName = System.getProperty("os.name");
		if (osName.startsWith("SunOS")) {
			PLATFORM_NAME = "solaris";
		}
	}

	static {
		overrideStandardStreams();
	}

	public static final String elPrintStr = String.format(
			"[EmergencyLanding-%s]", DisplayLayer.VERSION);

	public static void print(String msg) {
		try {
			checkAccessor(ACCEPT, StackTraceInfo.getInvokingClassName());
		} catch (Exception e) {
			throw new RuntimeException(new IllegalAccessException(
					"Not EL trusted class"));
		}
		System.err.println(elPrintStr + " " + msg);
	}

	private static void injectNatives() {
		System.setProperty("org.lwjgl.librarypath", LUtils.getELTop()
				+ File.separator + "res" + File.separator + "libs"
				+ File.separator + "natives" + File.separator + PLATFORM_NAME);
	}

	private static void overrideStandardStreams() {
		System.err.println("Replacing streams with methodized...");
		MethodizedSTDStream sysout = new MethodizedSTDStream(System.out);
		System.setOut(new PrintStream(sysout));
		MethodizedSTDStream syserr = new MethodizedSTDStream(System.err);
		System.setErr(new PrintStream(syserr));
		syserr.orig.println("Finished.");
	}

	/**
	 * The top level of the game/tool
	 */
	public static String TOP_LEVEL = null;
	static {
		try {
			// reuse KCore's data
			LUtils.TOP_LEVEL = Helper.Files.topLevel.replaceFirst("/$", "");
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
				.replace('/', File.separatorChar).substring(1)
				.replace("%20", " ");
		for (int i = 0; i < levels; i++) {
			tempName = tempName.substring(0,
					tempName.lastIndexOf(File.separatorChar));
		}
		EL_TOP = (tempName.startsWith(File.separator) ? "" : File.separator)
				+ tempName;
		LUtils.print("Using EL_TOP " + EL_TOP);

		injectNatives();
	}

	public static final int debugLevel = Integer.parseInt(System.getProperty(
			"el.debug.level", "0"));

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
		return Boolean.valueOf(LUtils.getArgS(args, index, Boolean.valueOf(def)
				.toString()));
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
		return Integer.valueOf(LUtils.getArgS(args, index, Integer.valueOf(def)
				.toString()));
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
		return Float.valueOf(LUtils.getArgS(args, index, Float.valueOf(def)
				.toString()));
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
		return Double.valueOf(LUtils.getArgS(args, index, Double.valueOf(def)
				.toString()));
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
		return args.length <= index ? def : args[index] == null ? def
				: args[index];
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
		return src.length <= index ? def : src[index] == null ? def
				: src[index];
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
	 * Check to see if access is allowed from the given class
	 * 
	 * @param accepts
	 *            - the packages to allow access from
	 * @param className
	 *            - the name of the class, including package (eg.
	 *            java.lang.String)
	 * @throws Exception
	 *             if any exceptions occur, they will be thrown
	 */
	public static void checkAccessor(String[] accepts, String className)
			throws Exception {
		boolean oneDidntThrow = false;
		for (int i = 0; i < accepts.length; i++) {
			String s = accepts[i];
			try {
				LUtils.checkAccessor(s, className);
				oneDidntThrow = true;
			} catch (Exception e) {
				if (e instanceof IllegalArgumentException) {
					accepts[i] += " --(DEBUG: This threw a IAE)--";
				}
				continue;
			}
		}
		if (oneDidntThrow) {
			return;
		}
		throw new IllegalAccessException("Access denied to " + className
				+ " because it wasn't in the following list: "
				+ Helper.BetterArrays.dump0(accepts));
	}

	/**
	 * Check to see if access is allowed from the given class
	 * 
	 * Accepts stars in the package name, such as java.lang.*
	 * 
	 * @param accept
	 *            - the package to allow access from
	 * @param className
	 *            - the name of the class, including package (eg.
	 *            java.lang.String)
	 * @throws Exception
	 *             if any exceptions occur, they will be thrown
	 */
	public static void checkAccessor(String accept, String className)
			throws Exception {
		int star = accept.indexOf('*'); // Star in package name
		if (star > -1 && accept.length() == 1) {
			// If any package is accepted, it's okay.
			return;
		}
		Class.forName(className); // make sure this is a REAL class
		if (star > -1) {
			// Any packages within the specified package
			if (accept.charAt(star - 1) != '.') {
				// Weird (invalid) package ex. com.package*.malformed
				throw new IllegalArgumentException("Package malformed");
			}
			String sub = accept.substring(0, star - 1);
			if (className.startsWith(sub)) {
				return;
			}
			throw new IllegalAccessException("Access denied to " + className
					+ " because it wasn't in " + accept);
		}
	}

	/**
	 * Attempts to get a fullscreen compatible {@link DisplayMode} for the width
	 * and height given
	 * 
	 * @param width
	 * @param height
	 * @param fullscreen
	 * @return
	 */
	public static DisplayMode getDisplayMode(int width, int height,
			boolean fullscreen) {
		try {
			for (DisplayMode m : Display.getAvailableDisplayModes()) {
				if (m.isFullscreenCapable() || !fullscreen) {
					if (m.getWidth() == width) {
						if (m.getHeight() == height) {
							if (m.isFullscreenCapable()) {
								return m;
							} else {
								LUtils.print(String
										.format("A non-aspect-compat mode"
												+ " is being used:"
												+ " Width: %s Height: %s"
												+ " Fullscreen: %s."
												+ " Fullscreen may not work as expected!",
												width, height, fullscreen));
							}
						}
					}
				}
				if (m.isFullscreenCapable()) {
					LUtils.print(String.format("A non-args-compat mode"
							+ " is avaliable:" + " Width: %s Height: %s"
							+ " Fullscreen: %s", m.getWidth(), m.getHeight(),
							m.isFullscreenCapable()));
				}
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		return new DisplayMode(width, height);
	}

	/**
	 * Returns a list of fullscreen capable dimensions
	 * 
	 * @return a list of fullscreen capable dimensions
	 */
	public static Dimension[] getFullscreenCompatDimensions() {
		try {
			ArrayList<Dimension> ret = new ArrayList<Dimension>();
			for (DisplayMode m : Display.getAvailableDisplayModes()) {
				if (m.isFullscreenCapable()) {
					ret.add(new Dimension(m.getWidth(), m.getHeight()));
				}
			}
			return ret.toArray(new Dimension[ret.size()]);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		return new Dimension[0];
	}

	/**
	 * Returns a list of all dimensions built into LWJGL
	 * 
	 * @return the list of all dimensions built into LWJGL
	 */
	public static Dimension[] getDimensions() {
		try {
			ArrayList<Dimension> ret = new ArrayList<Dimension>();
			for (DisplayMode m : Display.getAvailableDisplayModes()) {
				ret.add(new Dimension(m.getWidth(), m.getHeight()));
			}
			return ret.toArray(new Dimension[ret.size()]);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		return new Dimension[0];
	}

	/**
	 * Returns a user friendly version of the fullscreen compatible dimensions
	 * 
	 * @return a user friendly version of the fullscreen compatible dimensions
	 */
	public static String[] getFullscreenCompatDimensionsSimple() {
		return LUtils.getDimensionsSimple(LUtils
				.getFullscreenCompatDimensions());
	}

	/**
	 * Returns a list of Strings representing the Dimensions given in a user
	 * friendly form
	 * 
	 * @param compat
	 *            - the Dimensions to format
	 * @return a list of Strings representing the Dimensions in the form "W x H"
	 */
	public static String[] getDimensionsSimple(Dimension[] compat) {
		Dimension[] cmpt = compat;
		String[] s = new String[cmpt.length];
		for (int i = 0; i < cmpt.length; i++) {
			Dimension d = cmpt[i];
			s[i] = String.format("%s x %s", d.width, d.height);
		}
		return s;
	}

	/**
	 * Gets a fullscreen compatible dimension from the user
	 * 
	 * @return a fullscreen compatible dimension
	 */
	public static Dimension getDimensionFromUser() {
		return LUtils.getDimensionFromUser(LUtils
				.getFullscreenCompatDimensions());
	}

	/**
	 * Gets a dimension from the user, using the given list
	 * 
	 * @param availabeDimensions
	 *            - the dimensions to choose from
	 * @return
	 */
	public static Dimension getDimensionFromUser(Dimension[] availabeDimensions) {
		Dimension[] compat = availabeDimensions;
		String[] compat_s = LUtils.getDimensionsSimple(compat);
		JFrame toClose = null;
		String ret_s = (String) JOptionPane.showInputDialog(
				toClose = new JFrame(), "Avaliable sizes:",
				"Choose a window size", JOptionPane.DEFAULT_OPTION, null,
				compat_s, compat_s[0]);
		toClose.dispose();
		toClose = null;
		if (ret_s == null) {
			return null;
		}
		return compat[Arrays.asList(compat_s).indexOf(ret_s)];
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
	 * Gets a dimension from the args, or, failing that, the user
	 * 
	 * @param normalized
	 *            - 'normalized' argument list, (eg. ["-width", "800",
	 *            "-height", "600"])
	 * @return the dimension that was found or requested
	 */
	public static Dimension getDimensionFromUserAndArgs(String[] normalized) {
		return LUtils.getDimensionFromUserAndArgs(
				LUtils.getFullscreenCompatDimensions(), normalized);
	}

	/**
	 * Gets a dimension from the args, or, failing that, the user
	 * 
	 * @param dimensions
	 *            - the array of Dimensions to use
	 * @param normalized
	 *            - 'normalized' argument list, (eg. ["-width", "800",
	 *            "-height", "600"])
	 * @return the dimension that was found or requested
	 */
	public static Dimension getDimensionFromUserAndArgs(Dimension[] dimensions,
			String[] normalized) {
		if (normalized.length >= 4) {
			LUtils.print("This is the args sector");
			List<String> strs = Arrays.asList(normalized);
			LUtils.print(strs.toString());
			if (strs.indexOf("-width") == -1 || strs.indexOf("-height") == -1) {
			} else {
				String w = strs.get(strs.indexOf("-width") + 1);
				String h = strs.get(strs.indexOf("-height") + 1);
				if (LUtils.isInt(w) && LUtils.isInt(h)) {
					return new Dimension(Integer.parseInt(w),
							Integer.parseInt(h));
				}
			}
		}
		Dimension get = LUtils.getDimensionFromUser(dimensions);
		if (get == null) {
			LUtils.print("This is the args length " + normalized.length);
			get = new Dimension(600, 600);
		}

		return get;
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
	public static InputStream getInputStream(String path) throws IOException {
		LUtils.print("[Retriving InputStream for '" + path + "']");
		// Normalize to UNIX style
		path = path.replace(File.separatorChar, '/');

		InputStream result = null;

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
			result = new FileInputStream(path);
		} else if (isType == 1 || isType == 2) {
			LUtils.print("Using recursive zip/jar searcher style " + isType);
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			for (int i = 0; i < pathparts.size(); i++) {
				if (pathparts.get(i).endsWith(".zip")
						|| pathparts.get(i).endsWith(".jar")) {
					LUtils.print("Adding zip/jar " + pathparts.get(i) + " at "
							+ i);
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
			String file = pathToCurrFile.substring(0,
					pathToCurrFile.length() - 1);
			String extra = path.replace(pathToCurrFile, "");
			LUtils.print("Attempting to load from " + file);
			ZipFile zf = new ZipFile(file);
			ZipEntry ze = zf.getEntry(extra);
			result = zf.getInputStream(ze);
		}

		LUtils.print("[Complete]");
		return result;
	}

	/**
	 * Protected method to access EL's top level
	 */
	public static String getELTop() {
		try {
			checkAccessor(ACCEPT, StackTraceInfo.getInvokingClassName());
		} catch (Exception e) {
			throw new RuntimeException(new IllegalAccessException(
					"Not EL trusted class"));
		}
		return EL_TOP;
	}
}
