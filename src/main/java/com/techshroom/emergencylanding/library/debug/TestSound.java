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
//package com.techshroom.emergencylanding.library.debug;
//
//import java.util.HashMap;
//
//import org.lwjgl.opengl.Display;
//
//import com.techshroom.emergencylanding.library.internalstate.ELEntity;
//import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
//import com.techshroom.emergencylanding.library.lwjgl.render.Render;
//import com.techshroom.emergencylanding.library.main.KMain;
//import com.techshroom.emergencylanding.library.sound.SoundPlayer;
//import com.techshroom.emergencylanding.library.util.LUtils;
//
//public class TestSound extends KMain {
//
//    public static void main(String[] args) throws Exception {
//        DisplayLayer.initDisplay(false, 800, 600, "Testing EL Sound", true,
//                args);
//        while (!Display.isCloseRequested()) {
//            DisplayLayer.loop(120);
//        }
//        DisplayLayer.destroy();
//        System.exit(0);
//    }
//
//    @Override
//    public void onDisplayUpdate(int delta) {
//        DisplayLayer.readDevices();
//    }
//
//    @Override
//    public void init(DisplayLayer layer, String[] args) {
//        SoundPlayer.playWAV(LUtils.getELTop() + "/wav/test.wav", 1.0f,
//                .50f, true);
//        SoundPlayer.playWAV(LUtils.getELTop() + "/wav/test.wav", 1.0f,
//                .10f, true);
//        SoundPlayer.playWAV(LUtils.getELTop() + "/wav/test.wav", 1.0f,
//                2.15f, true);
//    }
//
//    @Override
//    public void registerRenders(
//            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
//
//    }
//}
