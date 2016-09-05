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
package com.techshroom.emergencylanding.library.lwjgl.render;

import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.util.DrawableUtils;
import com.techshroom.emergencylanding.library.util.LUtils;

public class TextureRender extends Render<ELEntity> {

    @Override
    public void doRender(ELEntity entity, float posX, float posY, float posZ) {
        DrawableUtils.beginStandardEntityRender(entity, posX, posY, posZ);
        VBAO quad = Shapes.getQuad(new VertexData(),
                new VertexData().setXYZ(entity.getTex().getWidth(), entity.getTex().getHeight(), 0),
                Shapes.XY);
        quad.setTexture(entity.getTex());
        quad.setStatic(false);
        if (quad.draw() == null && LUtils.debugLevel >= 1) {
            System.err.println("Failed to draw " + entity + ", quad said it was invalid");
        }
        quad.destroy();
        DrawableUtils.endStandardEntityRender();
    }

}
