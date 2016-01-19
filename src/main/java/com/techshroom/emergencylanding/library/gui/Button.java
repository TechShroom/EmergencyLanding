package com.techshroom.emergencylanding.library.gui;

import java.awt.geom.Rectangle2D;

import com.techshroom.emergencylanding.library.eventful.gui.ButtonEvent;
import com.techshroom.emergencylanding.library.eventful.window.MouseEvent;
import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.lwjgl.render.VBAO;
import com.techshroom.emergencylanding.library.lwjgl.render.VertexData;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;
import com.techshroom.emergencylanding.library.util.DrawableUtils;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

public class Button extends GuiElement {

    private final MBassador<ButtonEvent> eventBus = new MBassador<>();
    private final VBAO unpressedQuad;
    private final VBAO pressedQuad;
    private final double xLen;
    private final double yLen;
    private volatile boolean pressed;

    public Button(double x, double y, double xLength, double yLength,
            ELTexture unpressedT, ELTexture pressedT) {
        super(x, y);
        this.xLen = xLength;
        this.yLen = yLength;
        this.unpressedQuad = Shapes.getQuad(new VertexData().setXYZ(0, 0, 0),
                new VertexData().setXYZ((float) this.xLen, (float) this.yLen,
                        0),
                Shapes.XY);
        this.unpressedQuad.setTexture(unpressedT);
        this.unpressedQuad.setStatic(false);
        this.pressedQuad = Shapes.getQuad(new VertexData().setXYZ(0, 0, 0),
                new VertexData().setXYZ((float) this.xLen, (float) this.yLen,
                        0),
                Shapes.XY);
        this.pressedQuad.setTexture(pressedT);
        this.pressedQuad.setStatic(false);
    }

    @Override
    public void draw() {
        DrawableUtils.glBeginTrans(this.xPos, this.xPos, 0);
        (this.pressed ? this.pressedQuad : this.unpressedQuad).draw();
        DrawableUtils.glEndTrans();
    }

    @Override
    public void update() {
    }

    @Handler
    protected void onMouseClick(MouseEvent.Click event) {
        Rectangle2D rect = new Rectangle2D.Double(this.xPos, this.xPos,
                this.xLen, this.yLen);
        if (rect.contains(event.getX(), event.getY())) {
            boolean wasPressed = this.pressed;
            if (event instanceof MouseEvent.Click.Press) {
                this.pressed = true;
            } else if (event instanceof MouseEvent.Click.Release) {
                this.pressed = false;
                if (!wasPressed) {
                    // TODO fire event.
                }
            }
        }
    }

}
