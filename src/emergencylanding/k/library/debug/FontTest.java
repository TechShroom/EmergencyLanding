package emergencylanding.k.library.debug;

import java.awt.Font;
import java.util.HashMap;

import org.lwjgl.opengl.Display;

import emergencylanding.k.library.internalstate.Entity;
import emergencylanding.k.library.lwjgl.DisplayLayer;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.lwjgl.render.StringRenderer;
import emergencylanding.k.library.main.KMain;

public class FontTest extends KMain {
	static StringRenderer strrend;

	public static void main(String[] args) throws Exception {
		DisplayLayer.initDisplay(false, 800, 600, "Fonts!", false, args);
		while (!Display.isCloseRequested()) {
			DisplayLayer.loop(120);
		}
		strrend.destroy();
		DisplayLayer.destroy();
		System.exit(0);
	}

	@Override
	public void onDisplayUpdate(int delta) {
		strrend.drawString(100, 100, "Font is TNR Bold!!", 1, 1);
	}

	@Override
	public void init(String[] args) {
		strrend = new StringRenderer(
				new Font("times new roman", Font.BOLD, 16), false);
	}

	@Override
	public void registerRenders(
			HashMap<Class<? extends Entity>, Render<? extends Entity>> classToRender) {
		// TODO Auto-generated method stub

	}
}
