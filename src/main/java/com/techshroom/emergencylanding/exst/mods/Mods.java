/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <http://techshoom.com>
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
package com.techshroom.emergencylanding.exst.mods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.techshroom.emergencylanding.exst.modules.ModuleSystem;
import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.lwjgl.render.Render;
import com.techshroom.emergencylanding.library.main.KMain;
import com.techshroom.emergencylanding.library.util.ClassPathHack;
import com.techshroom.emergencylanding.library.util.LUtils;

public class Mods {

    private static ArrayList<IMod> loaded = new ArrayList<IMod>();
    private static boolean loaded_mods = false;

    public static void findAndLoad() {
        if (loaded_mods) {
            System.err.println(
                    "Already loaded mod system, trying to load again?");
            return;
        }
        System.err.println("EL Mod System starting...");
        if (!injectModsFolder()) {
            System.err.println(
                    "[WARNING] Mods folder does not exist or is a file, "
                            + "add it if you want mods to be loaded from there.");
        }
        ArrayList<IMod> injected = ModInjector.findAndInject();
        System.err.println("Loaded mods from classpath.");
        System.err.println("Letting game mess with mods list...");
        KMain inst = KMain.getInst();
        inst.loadMods(injected);
        System.err.println("Complete.");
        loaded.addAll(injected);
        System.err.println("Initializing mods...");
        for (IMod m : loaded) {
            try {
                m.init(inst);
            } catch (Exception e) {
                System.err
                        .println("Error registering mod " + m.getClass() + ":");
                e.printStackTrace();
                injected.remove(m);
            }
        }
        System.err.println("Complete.");
        loaded = injected;
        loaded.trimToSize();
        loaded_mods = true;
        System.err.println("EL Mod System loaded.");
        ModuleSystem.loadModulesFromMods();
    }

    private static boolean injectModsFolder() {
        String topLevel = LUtils.TOP_LEVEL;
        File mods = new File(topLevel, "mods");
        if (!loadDirectory(mods)) {
            return false;
        }
        System.err.println("Injected '" + mods + "' into classpath.");
        return true;
    }

    private static boolean loadDirectory(File dir) {
        if (!dir.exists() || dir.isFile()) {
            return false;
        }

        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                loadDirectory(f);
                continue;
            }
            try {
                ClassPathHack.addFile(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
        if (!loaded_mods) {
            throw new IllegalStateException(
                    "Registering renderers before loading mods!");
        }
        for (IMod m : loaded) {
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> tmp =
                    new HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>>();
            m.registerRenders(tmp);
            classToRender.putAll(tmp);
        }
    }

    public static List<IMod> getLoadedMods() {
        if (!loaded_mods) {
            throw new IllegalStateException(
                    "Getting loaded mods list before loading mods!");
        }
        return Collections.unmodifiableList(loaded);
    }
}
