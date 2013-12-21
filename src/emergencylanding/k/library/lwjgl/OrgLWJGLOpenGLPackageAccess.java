package emergencylanding.k.library.lwjgl;

import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.lwjgl.opengl.Display;

import emergencylanding.k.library.util.LUtils;

public class OrgLWJGLOpenGLPackageAccess {
    static Object impl = null; // ACTUALLY a DisplayImplementaion
    static Method updateImpl = null, pollDevices = null;

    public static void updateImplementation() {
	if (impl == null) {
	    try {
		Method getI = Display.class
			.getDeclaredMethod("getImplementation");
		getI.setAccessible(true);
		impl = getI.invoke(null);
		updateImpl = impl.getClass().getDeclaredMethod("update");
		updateImpl.setAccessible(true);
		LUtils.print("LWJGL's implementation is secured.");
	    } catch (Exception e) {
		throw new IllegalStateException(
			"Implementation not accessable", e);
	    }
	}
	try {
	    updateImpl.invoke(impl);
	} catch (IllegalArgumentException e) {
	    throw new IllegalArgumentException("Bad implementation", e);
	} catch (IllegalAccessException e) {
	    throw new IllegalStateException("Could not access update method", e);
	} catch (InvocationTargetException e) {
	    throw new RuntimeException("Error during impl update", e);
	}
    }

    public static void pollDevices() {
	if (pollDevices == null) {
	    try {
		pollDevices = Display.class.getDeclaredMethod("pollDevices");
		pollDevices.setAccessible(true);
	    } catch (SecurityException e) {
		e.printStackTrace();
	    } catch (NoSuchMethodException e) {
		throw new RuntimeException(new IllegalClassFormatException(
			"pollDevices not found"));
	    }
	}
	try {
	    pollDevices.invoke(null);
	} catch (IllegalArgumentException e) {
	    throw new IllegalArgumentException(
		    "Impossible to have wrong argument", e);
	} catch (IllegalAccessException e) {
	    throw new IllegalStateException(
		    "Could not access pollDevices method", e);
	} catch (InvocationTargetException e) {
	    throw new RuntimeException("Error during device polling", e);
	}
    }

}
