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
package com.techshroom.emergencylanding.library.eventful;

import com.techshroom.emergencylanding.library.eventful.events.gui.ButtonEvent;
import com.techshroom.emergencylanding.library.eventful.events.render.NanoVGRenderEvent;
import com.techshroom.emergencylanding.library.eventful.events.render.RenderEvent;
import com.techshroom.emergencylanding.library.eventful.events.window.MouseEvent;
import com.techshroom.emergencylanding.library.eventful.events.window.WindowDestructionEvent;
import com.techshroom.emergencylanding.library.eventful.events.window.WindowEvent;
import com.techshroom.emergencylanding.library.eventful.events.window.WindowTitleChange;
import com.techshroom.emergencylanding.library.gui.Button;
import com.techshroom.emergencylanding.library.lwjgl.control.Cursor;
import java.util.HashMap;

public class EmergencyLandingEventFactory {
    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.gui.ButtonEvent}.
     * 
     * @param button The button
     * @return A new button event
     */
    public static ButtonEvent createButtonEvent(Button button) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("button", button);
        return EmergencyLandingEventFactoryUtils.createEventImpl(ButtonEvent.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.gui.ButtonEvent.Click}.
     * 
     * @param button The button
     * @return A new click button event
     */
    public static ButtonEvent.Click createButtonEventClick(Button button) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("button", button);
        return EmergencyLandingEventFactoryUtils.createEventImpl(ButtonEvent.Click.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.render.NanoVGRenderEvent}.
     * 
     * @param renderDelta The render delta
     * @return A new nano v g render event
     */
    public static NanoVGRenderEvent createNanoVGRenderEvent(long renderDelta) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("renderDelta", renderDelta);
        return EmergencyLandingEventFactoryUtils.createEventImpl(NanoVGRenderEvent.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.render.NanoVGRenderEvent.Begin}.
     * 
     * @param renderDelta The render delta
     * @return A new begin nano v g render event
     */
    public static NanoVGRenderEvent.Begin createNanoVGRenderEventBegin(long renderDelta) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("renderDelta", renderDelta);
        return EmergencyLandingEventFactoryUtils.createEventImpl(NanoVGRenderEvent.Begin.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.render.NanoVGRenderEvent.End}.
     * 
     * @param renderDelta The render delta
     * @return A new end nano v g render event
     */
    public static NanoVGRenderEvent.End createNanoVGRenderEventEnd(long renderDelta) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("renderDelta", renderDelta);
        return EmergencyLandingEventFactoryUtils.createEventImpl(NanoVGRenderEvent.End.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.render.RenderEvent}.
     * 
     * @param renderDelta The render delta
     * @return A new render event
     */
    public static RenderEvent createRenderEvent(long renderDelta) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("renderDelta", renderDelta);
        return EmergencyLandingEventFactoryUtils.createEventImpl(RenderEvent.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.render.RenderEvent.Setup}.
     * 
     * @param renderDelta The render delta
     * @return A new setup render event
     */
    public static RenderEvent.Setup createRenderEventSetup(long renderDelta) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("renderDelta", renderDelta);
        return EmergencyLandingEventFactoryUtils.createEventImpl(RenderEvent.Setup.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.render.RenderEvent.TearDown}.
     * 
     * @param renderDelta The render delta
     * @return A new tear down render event
     */
    public static RenderEvent.TearDown createRenderEventTearDown(long renderDelta) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("renderDelta", renderDelta);
        return EmergencyLandingEventFactoryUtils.createEventImpl(RenderEvent.TearDown.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.window.MouseEvent}.
     * 
     * @return A new mouse event
     */
    public static MouseEvent createMouseEvent() {
        HashMap<String, Object> values = new HashMap<>();
        return EmergencyLandingEventFactoryUtils.createEventImpl(MouseEvent.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.window.MouseEvent.Click}.
     * 
     * @param modifiers The modifiers
     * @param mouseButton The mouse button
     * @param x The x
     * @param y The y
     * @return A new click mouse event
     */
    public static MouseEvent.Click createMouseEventClick(int modifiers, int mouseButton, double x, double y) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("modifiers", modifiers);
        values.put("mouseButton", mouseButton);
        values.put("x", x);
        values.put("y", y);
        return EmergencyLandingEventFactoryUtils.createEventImpl(MouseEvent.Click.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.window.MouseEvent.Click.Press}.
     * 
     * @param modifiers The modifiers
     * @param mouseButton The mouse button
     * @param x The x
     * @param y The y
     * @return A new press click mouse event
     */
    public static MouseEvent.Click.Press createMouseEventClickPress(int modifiers, int mouseButton, double x, double y) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("modifiers", modifiers);
        values.put("mouseButton", mouseButton);
        values.put("x", x);
        values.put("y", y);
        return EmergencyLandingEventFactoryUtils.createEventImpl(MouseEvent.Click.Press.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.window.MouseEvent.Click.Release}.
     * 
     * @param modifiers The modifiers
     * @param mouseButton The mouse button
     * @param x The x
     * @param y The y
     * @return A new release click mouse event
     */
    public static MouseEvent.Click.Release createMouseEventClickRelease(int modifiers, int mouseButton, double x, double y) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("modifiers", modifiers);
        values.put("mouseButton", mouseButton);
        values.put("x", x);
        values.put("y", y);
        return EmergencyLandingEventFactoryUtils.createEventImpl(MouseEvent.Click.Release.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.window.MouseEvent.Click.Repeat}.
     * 
     * @param modifiers The modifiers
     * @param mouseButton The mouse button
     * @param x The x
     * @param y The y
     * @return A new repeat click mouse event
     */
    public static MouseEvent.Click.Repeat createMouseEventClickRepeat(int modifiers, int mouseButton, double x, double y) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("modifiers", modifiers);
        values.put("mouseButton", mouseButton);
        values.put("x", x);
        values.put("y", y);
        return EmergencyLandingEventFactoryUtils.createEventImpl(MouseEvent.Click.Repeat.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.window.MouseEvent.CursorChange}.
     * 
     * @param newCursor The new cursor
     * @param oldCursor The old cursor
     * @return A new cursor change mouse event
     */
    public static MouseEvent.CursorChange createMouseEventCursorChange(Cursor newCursor, Cursor oldCursor) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("newCursor", newCursor);
        values.put("oldCursor", oldCursor);
        return EmergencyLandingEventFactoryUtils.createEventImpl(MouseEvent.CursorChange.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.window.MouseEvent.Motion}.
     * 
     * @param dX The d x
     * @param dY The d y
     * @param newX The new x
     * @param newY The new y
     * @param oldX The old x
     * @param oldY The old y
     * @return A new motion mouse event
     */
    public static MouseEvent.Motion createMouseEventMotion(double dX, double dY, double newX, double newY, double oldX, double oldY) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("dX", dX);
        values.put("dY", dY);
        values.put("newX", newX);
        values.put("newY", newY);
        values.put("oldX", oldX);
        values.put("oldY", oldY);
        return EmergencyLandingEventFactoryUtils.createEventImpl(MouseEvent.Motion.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.window.WindowDestructionEvent}.
     * 
     * @param window The window
     * @return A new window destruction event
     */
    public static WindowDestructionEvent createWindowDestructionEvent(long window) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("window", window);
        return EmergencyLandingEventFactoryUtils.createEventImpl(WindowDestructionEvent.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.window.WindowDestructionEvent.Post}.
     * 
     * @param window The window
     * @return A new post window destruction event
     */
    public static WindowDestructionEvent.Post createWindowDestructionEventPost(long window) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("window", window);
        return EmergencyLandingEventFactoryUtils.createEventImpl(WindowDestructionEvent.Post.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.window.WindowDestructionEvent.Pre}.
     * 
     * @param window The window
     * @return A new pre window destruction event
     */
    public static WindowDestructionEvent.Pre createWindowDestructionEventPre(long window) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("window", window);
        return EmergencyLandingEventFactoryUtils.createEventImpl(WindowDestructionEvent.Pre.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.window.WindowEvent}.
     * 
     * @param window The window
     * @return A new window event
     */
    public static WindowEvent createWindowEvent(long window) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("window", window);
        return EmergencyLandingEventFactoryUtils.createEventImpl(WindowEvent.class, values);
    }

    /**
     * AUTOMATICALLY GENERATED, DO NOT EDIT.
     * Creates a new instance of
     * {@link com.techshroom.emergencylanding.library.eventful.events.window.WindowTitleChange}.
     * 
     * @param newTitle The new title
     * @param oldTitle The old title
     * @param window The window
     * @return A new window title change
     */
    public static WindowTitleChange createWindowTitleChange(String newTitle, String oldTitle, long window) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("newTitle", newTitle);
        values.put("oldTitle", oldTitle);
        values.put("window", window);
        return EmergencyLandingEventFactoryUtils.createEventImpl(WindowTitleChange.class, values);
    }
}

