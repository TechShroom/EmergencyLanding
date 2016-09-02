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
package com.techshroom.emergencylanding.library.debug;

import com.techshroom.emergencylanding.imported.Color;
import com.techshroom.emergencylanding.imported.Sync.RunningAvg;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.lwjgl.render.VBAO;
import com.techshroom.emergencylanding.library.lwjgl.render.VertexData;
import com.techshroom.emergencylanding.library.lwjgl.tex.ColorTexture;
import com.techshroom.emergencylanding.library.main.KMain;
import com.techshroom.emergencylanding.library.util.DrawableUtils;

public class VBAOTest extends KMain {

    private static DisplayLayer layer;

    VBAO quad = null;
    RunningAvg davg = new RunningAvg(10);

    int t = 0;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try {
            layer = DisplayLayer.initDisplay(0, 1000, 600, "VBAO NewElTest",
                    false, false, args);
            layer.getDisplayFPSTracker().enable(layer.getWindow());
            while (!layer.shouldClose()) {
                layer.loop(120000);
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
        DrawableUtils.glBeginTrans(delta, delta, delta);
        this.quad.draw();
        DrawableUtils.glEndTrans();
    }

    @Override
    public void init(DisplayLayer layer, String[] args) {
        float[] v1 = { 50, 200, 0, 1f, 1f, 1f };
        float[] v2 = { 50, 50, 0, 1f, 1f, 1f };
        float[] v3 = { 200, 50, 0, 1f, 1f, 1f };
        float[] v4 = { 200, 200, 0, 1f, 1f, 1f };
        int order = VertexData.COLOR_FIRST;
        VertexData[] verts =
                { new VertexData(order, v1), new VertexData(order, v2),
                        new VertexData(order, v3), new VertexData(order, v4) };
        this.quad = Shapes.getQuad(verts);
        this.quad.setTexture(new ColorTexture(Color.BLUE));
        this.quad.setStatic(false);
    }

}
