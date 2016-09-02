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

import java.util.HashMap;

import com.techshroom.emergencylanding.exst.mods.Mods;
import com.techshroom.emergencylanding.library.internalstate.ELEntity;
import com.techshroom.emergencylanding.library.internalstate.Victor;
import com.techshroom.emergencylanding.library.internalstate.world.World;
import com.techshroom.emergencylanding.library.internalstate.world.WorldManager;
import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.main.KMain;
import com.techshroom.emergencylanding.library.util.BoundingBox;
import com.techshroom.emergencylanding.library.util.DrawableUtils;
import com.techshroom.emergencylanding.library.util.LUtils;

public class RenderManager {

    private static HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender =
            new HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>>();

    public static void registerRenders() {
        HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> temp =
                new HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>>();
        Mods.registerRenders(temp);
        classToRender.putAll(temp);
        temp = new HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>>();
        KMain.getInst().registerRenders(temp);
        classToRender.putAll(temp);
        RenderManager.classToRender.put(ELEntity.class, new DefaultRender());
    }

    public static void doRender(World w, int delta) {
        for (ELEntity e : w.getEntityList()) {
            Victor p = e.getInterpolated();
            RenderManager.doRender(e, p);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void doRender(ELEntity e, Victor interpolated) {
        Render r = RenderManager.getRendererForClass(e.getClass());
        if (r == null) {
            throw new IllegalStateException("Missing default render!");
        }
        r.doRender(e, interpolated);
        if (LUtils.debugLevel >= 1) {
            BoundingBox bb = e.getBB();
            DrawableUtils.beginStandardEntityRender(e, interpolated.x,
                    interpolated.y, interpolated.z);
            VBAO bbbox = Shapes.getQuad(new VertexData().setRGB(255, 0, 0),
                    new VertexData().setXYZ((float) (bb.getWidth()),
                            (float) (bb.getHeight()), 0),
                    Shapes.XY);
            bbbox.setStatic(false);
            bbbox.draw().destroy();
            DrawableUtils.endStandardEntityRender();
        }
    }

    @SuppressWarnings("unchecked")
    public static Render<?> getRendererForClass(Class<? extends ELEntity> cls) {
        Render<?> r = null;
        if (RenderManager.classToRender.containsKey(cls)) {
            r = RenderManager.classToRender.get(cls);
        } else if (cls.getSuperclass() != null
                && ELEntity.class.isAssignableFrom(cls.getSuperclass())) {
            r = RenderManager.getRendererForClass(
                    (Class<? extends ELEntity>) cls.getSuperclass());
        }
        return r;
    }

    public static void render(int delta) {
        WorldManager.render(delta);
    }

}
