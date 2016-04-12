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

import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.main.KMain;
import com.techshroom.emergencylanding.library.sound.SoundPlayer;
import com.techshroom.emergencylanding.library.util.LUtils;

public class TestSound extends KMain {

    private static DisplayLayer layer;

    public static void main(String[] args) throws Exception {
        try {
            layer = DisplayLayer.initDisplay(0, 800, 600, "Testing EL Sound",
                    true, args);
            while (!layer.shouldClose()) {
                layer.loop(120);
            }
        } finally {
            if (layer != null) {
                layer.destroy();
            }
            DisplayLayer.finalExitCall();
        }
    }

    @Override
    public void onDisplayUpdate(int delta) {
    }

    @Override
    public void init(DisplayLayer layer, String[] args) {
        SoundPlayer player = new SoundPlayer();
        player.playWAV(LUtils.getELTop() + "/wav/test.wav", 1.0f, .50f, true);
        player.playWAV(LUtils.getELTop() + "/wav/test.wav", 1.0f, .10f, true);
        player.playWAV(LUtils.getELTop() + "/wav/test.wav", 1.0f, 2.15f, true);
    }

}
