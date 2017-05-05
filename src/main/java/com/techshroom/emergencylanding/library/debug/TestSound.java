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

import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.glfw.GLFW;

import com.google.common.io.Resources;
import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.lwjgl.render.VBAO;
import com.techshroom.emergencylanding.library.lwjgl.render.VertexData;
import com.techshroom.emergencylanding.library.main.KMain;
import com.techshroom.emergencylanding.library.sound.ALSound;
import com.techshroom.emergencylanding.library.sound.Sound;
import com.techshroom.emergencylanding.library.sound.SoundPlayer;

public class TestSound extends KMain {

    private static DisplayLayer layer;

    public static void main(String[] args) throws Exception {
        try {
            layer = DisplayLayer.initDisplay(0, 800, 600, "Testing EL Sound", true, args);
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

    private VBAO timeA;
    private VBAO timeB;
    private VBAO timeC;
    private Sound a;
    private Sound b;
    private Sound c;
    private int elapsed = 0;
    private int state = 0;
    private SoundPlayer player;

    @Override
    public void onDisplayUpdate(int delta) {
        this.timeA.draw();
        this.timeB.draw();
        this.timeC.draw();
        this.elapsed += delta;
        if (this.elapsed > 2000) {
            this.state++;
            this.elapsed = 0;
        }
        switch (this.state) {
            case 1:
                this.a.stop();
                break;
            case 2:
                this.b.stop();
                break;
            case 3:
                this.c.stop();
                break;
            case 4:
                this.player.close();
                GLFW.glfwSetWindowShouldClose(layer.getWindow(), true);
        }
    }

    @Override
    public void init(DisplayLayer layer, String[] args) {
        this.player = new SoundPlayer();
        InputStream stream;
        try {
            stream = Resources.asByteSource(Resources.getResource("ogg/test.ogg")).openBufferedStream();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ALSound base;
        try {
            base = this.player.getSoundClipFactory().create(() -> stream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.a = base.requiredCopy().setVolume(.5f).setPitch(.99f).setLooping(true).play();
//        this.timeA = Shapes.getQuad(new VertexData().setXYZ(50, 10, 0).setXYZ(x, y, z), new VertexData().setXYZ(50, 20, 0), Shapes.XY);
        this.b = base.requiredCopy().setVolume(.5f).setPitch(.98f).setLooping(true).play();
        this.timeB = Shapes.getQuad(new VertexData().setXYZ(50, 30, 0), new VertexData().setXYZ(50, 40, 0), Shapes.XY);
        this.c = base.requiredCopy().setVolume(.5f).setPitch(1).setLooping(true).play();
        this.timeC = Shapes.getQuad(new VertexData().setXYZ(50, 50, 0), new VertexData().setXYZ(50, 60, 0), Shapes.XY);
    }

}
