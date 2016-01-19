package com.techshroom.emergencylanding.library.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassPathHack {
    private static final Class<?>[] parameters = new Class[] { URL.class };

    /**
     * Adds the file represented by the given String to the classpath.
     * 
     * @param s
     *            - a file, represented as a String
     * @throws IOException
     *             if there are any problems injecting.
     */
    public static void addFile(String s) throws IOException {
        File f = new File(s);
        addFile(f);
    }

    /**
     * Adds the given {@link File} to the classpath.
     * 
     * @param f
     *            - a File
     * @throws IOException
     *             if there is any problems injecting.
     */
    public static void addFile(File f) throws IOException {
        addURL(f.toURI().toURL());
    }

    /**
     * Adds the given {@link URL} to the classpath.
     * 
     * @param u
     *            - a URL
     * @throws IOException
     *             if there is any problems injecting.
     */
    public static void addURL(URL u) throws IOException {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader
                .getSystemClassLoader();
        Class<?> sysclass = URLClassLoader.class;

        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { u });
        } catch (Throwable t) {
            throw new IOException(
                    "Error, could not add URL to system classloader", t);
        }

        System.setProperty("java.class.path",
                System.getProperty("java.class.path")
                        + File.pathSeparator
                        + u.getFile().replace('/', File.separatorChar)
                                .substring(1).replace("%20", " "));
    }
}