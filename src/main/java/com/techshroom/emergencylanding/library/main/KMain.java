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
package com.techshroom.emergencylanding.library.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.techshroom.emergencylanding.exst.mods.IMod;
import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.render.Render;
import com.techshroom.emergencylanding.library.util.LUtils;

public abstract class KMain {

    private static KMain insts = null;
    private static Thread displayThread = null;

    public abstract void onDisplayUpdate(int delta);

    public abstract void init(DisplayLayer layer, String[] args);

    public static void setInst(KMain inst) {
        KMain.insts = inst;
    }

    public static KMain getInst() {
        return KMain.insts;
    }

    public static void setDisplayThread(Thread t) {
        KMain.displayThread = t;
        LUtils.print("Display thread set");
    }

    public static Thread getDisplayThread() {
        return KMain.displayThread;
    }

    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
    }

    public void loadMods(ArrayList<IMod> mods) {

    }
}
