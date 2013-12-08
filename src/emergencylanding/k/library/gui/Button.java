package emergencylanding.k.library.gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.Action;

import emergencylanding.k.library.lwjgl.Shapes;
import emergencylanding.k.library.lwjgl.control.MouseHelp;
import emergencylanding.k.library.lwjgl.tex.Texture;

public class Button extends GuiElement {

	private Texture unpressedTexture, pressedTexture;
	private float xLen, yLen;
	private volatile boolean pressed;
	private Action onC = null;

	public Button(float x, float y, float xLength, float yLength,
			Texture unpressedT, Texture pressedT, Action onClick) {
		super(x, y);
		xLen = xLength;
		yLen = yLength;
		unpressedTexture = unpressedT;
		pressedTexture = pressedT;
		onC = onClick;
	}

	@Override
	public void drawAt(float x, float y) {
		if (pressed) {
			Shapes.glQuad(x, y, 0, xLen, yLen, 0, Shapes.XYF, pressedTexture);
		} else {
			Shapes.glQuad(x, y, 0, xLen, yLen, 0, Shapes.XYF, unpressedTexture);
		}
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
