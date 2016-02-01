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

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.lwjgl.glfw.GLFW;

import com.flowpowered.math.vector.Vector2f;
import com.google.common.base.Throwables;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.render.VBAO;
import com.techshroom.emergencylanding.library.lwjgl.render.string.FontStorage.FontRenderingData;
import com.techshroom.emergencylanding.library.lwjgl.render.string.StringRenderer;
import com.techshroom.emergencylanding.library.main.KMain;

public class FontTest extends KMain {

    static StringRenderer strrend;
    static VBAO image = null;
    private static DisplayLayer layer;

    public static void main(String[] args) throws Exception {
        try {
            layer = DisplayLayer.initDisplay(0, 800, 600, "Fonts!", false, true,
                    args);
            layer.getDisplayFPSTracker().enable(layer.getWindow());
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
        if (strrend == null) {
            try {
                strrend = FontRenderingData.create(() -> {
                    try {
                        return FileChannel.open(getFontPath());
                    } catch (IOException e) {
                        throw Throwables.propagate(e);
                    }
                }, 24).getStringRenderer();
            } catch (IOException e) {
                e.printStackTrace();
                GLFW.glfwSetWindowShouldClose(layer.getWindow(),
                        GLFW.GLFW_TRUE);
            }
        }
        strrend.renderString("A", new Vector2f(200, 200));
        strrend.renderString("Font is TNR Bold!!\nlorem ipsum",
                new Vector2f(100, 100));
    }

    @Override
    public void init(DisplayLayer layer, String[] args) {
    }

    private Path getFontPath() {
        URL url =
                getClass().getProtectionDomain().getCodeSource().getLocation();
        try {
            return Paths.get(url.toURI()).resolve("fonts/anonpro.ttf");
        } catch (URISyntaxException e) {
            throw Throwables.propagate(e);
        }
    }

}
