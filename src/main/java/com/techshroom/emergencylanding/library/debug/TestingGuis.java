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
package com.techshroom.emergencylanding.library.debug;

import com.flowpowered.math.vector.Vector2i;
import com.techshroom.emergencylanding.imported.Color;
import com.techshroom.emergencylanding.imported.Sync;
import com.techshroom.emergencylanding.library.gui.GuiElement;
import com.techshroom.emergencylanding.library.gui.Screen;
import com.techshroom.emergencylanding.library.gui.Slider;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.tex.ColorTexture;
import com.techshroom.emergencylanding.library.main.KMain;
import com.techshroom.emergencylanding.library.util.LUtils;

public class TestingGuis extends KMain {

    private static DisplayLayer layer;
    private static Thread is;
    private static boolean run = true;
    private static final int TICKS_PER_SECOND = 120;
    private static Screen g;
    private static GuiElement slider;

    public static void main(String[] args) {
        try {
            layer = DisplayLayer.initDisplay(0, 800, 500,
                    "Testing " + LUtils.LIB_NAME, true, args);
            layer.getDisplayFPSTracker().enable(layer.getWindow());
            TestingGuis.startISThreads();
            System.err.println("percent of 656/1000 = "
                    + Slider.getPercentClosestToValue(656, 0, 1000));
            while (TestingGuis.run) {
                TestingGuis.run = !layer.shouldClose();
                layer.loop(120);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (layer != null) {
                layer.destroy();
            }
            DisplayLayer.finalExitCall();
        }
    }

    private static void startISThreads() {
        Runnable isr = new Runnable() {

            Sync s = new Sync();

            @Override
            public void run() {
                FPS fps = new FPS("is");
                fps.init();
                while (TestingGuis.run) {
                    this.s.sync(TestingGuis.TICKS_PER_SECOND);
                    fps.update();
                    TestingGuis.g.update();
                }
            }
        };
        TestingGuis.is = new Thread(isr);
        TestingGuis.is.setName("Internal State Thread");
        TestingGuis.is.start();
        System.err.println("ISThreads running!");
    }

    private String[] strvalues = new String[101];
    {
        this.strvalues[Slider.getPercentClosestToValue(0, 0, 1000)] = "Minimum";
        this.strvalues[Slider.getPercentClosestToValue(1000, 0, 1000)] =
                "Maximum";
    }

    @Override
    public void onDisplayUpdate(int delta) {
        TestingGuis.g.draw();
    }

    @Override
    public void init(DisplayLayer layer, String[] args) {
        TestingGuis.g = new Screen(layer.getWindow());
        TestingGuis.slider = new Slider(50, 50, 0, 1000,
                new ColorTexture(Color.RED, new Vector2i(10, 50)),
                new ColorTexture(Color.GREEN, new Vector2i(100, 50)), "Test: ",
                this.strvalues/*
                               * , new StringRenderer( new
                               * Font("times new roman", Font.PLAIN, 16), false)
                               */);
        g.addElement(slider);
    }

}
