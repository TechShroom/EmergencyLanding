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
package com.techshroom.emergencylanding.exst.modules;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.techshroom.emergencylanding.exst.mods.IMod;
import com.techshroom.emergencylanding.exst.mods.Mods;

public final class ModuleSystem {

    private static ArrayList<IModule> modules = new ArrayList<IModule>();

    private ModuleSystem() {
        throw new AssertionError("Please don't create the module system.");
    }

    public static void loadModulesFromMods() {
        List<IMod> loaded = Mods.getLoadedMods();
        for (IMod m : loaded) {
            if (m instanceof IModule) {
                IModule im = (IModule) m;
                modules.add(im);
            }
        }
    }

    /**
     * Gets the registered modules for the given class.
     * 
     * @param moduleClass
     *            - the parent class
     * @param <T>
     *            - The type of the modules to find
     * @return an array of {@link IModule} instances whose getClass() method
     *         either returns the same class as {@code moduleClass} or a class
     *         that extends that class.
     */
    public static <T extends IModule> T[] getRegisteredModules(Class<T> moduleClass) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(moduleClass, 0);
        if (moduleClass == null) {
            return array;
        }
        ArrayList<IModule> matched = new ArrayList<IModule>();
        for (IModule m : modules) {
            if (moduleClass.isInstance(m)) {
                matched.add(m);
            }
        }
        return matched.toArray(array);
    }
}
