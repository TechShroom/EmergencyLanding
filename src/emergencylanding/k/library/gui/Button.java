package emergencylanding.k.library.gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.Action;

import emergencylanding.k.library.internalstate.Victor;
import emergencylanding.k.library.lwjgl.Shapes;
import emergencylanding.k.library.lwjgl.control.MouseHelp;
import emergencylanding.k.library.lwjgl.render.VBAO;
import emergencylanding.k.library.lwjgl.render.VertexData;
import emergencylanding.k.library.lwjgl.tex.ELTexture;
import emergencylanding.k.library.util.DrawableUtils;

public class Button extends GuiElement {

    private VBAO unpressed, pressedv;
    private float xLen, yLen;
    private volatile boolean pressed;
    private Action onC = null;

    public Button(float x, float y, float xLength, float yLength,
            ELTexture unpressedT, ELTexture pressedT, Action onClick) {
        super(x, y);
        xLen = xLength;
        yLen = yLength;
        onC = onClick;
        unpressed = Shapes.getQuad(new VertexData().setXYZ(0, 0, 0),
                new VertexData().setXYZ(xLen, yLen, 0), Shapes.XY);
        unpressed.setTexture(unpressedT);
        pressedv = Shapes.getQuad(new VertexData().setXYZ(0, 0, 0),
                new VertexData().setXYZ(xLen, yLen, 0), Shapes.XY);
        pressedv.setTexture(pressedT);
    }

    @Override
    public void drawAt(float x, float y) {
        VBAO drawing = null;
        if (pressed) {
            drawing = pressedv;
        } else {
            drawing = unpressed;
        }
        DrawableUtils.glBeginTrans(x, y, 0);
        drawing.draw();
        DrawableUtils.glEndTrans();
    }

    @Override
    public void updateAt(float x, float y) {
        if (MouseHelp.clickingInRect(new Rectangle((int) x, (int) y,
                (int) xLen, (int) yLen), MouseHelp.ANY)) {
            pressed = true;
        } else {
            pressed = false;
        }
        if (MouseHelp.clickedInRect(new Rectangle((int) x, (int) y, (int) xLen,
                (int) yLen), MouseHelp.ANY)) {
            onC.actionPerformed(new ActionEvent(this, this.hashCode(),
                    "ButtonClick"));
        }
    }

}
