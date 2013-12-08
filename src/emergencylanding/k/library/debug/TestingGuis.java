package emergencylanding.k.library.debug;

import java.awt.Font;
import java.util.HashMap;

import org.lwjgl.opengl.Display;

import emergencylanding.k.imported.Sync;
import emergencylanding.k.library.gui.GuiElement;
import emergencylanding.k.library.gui.Slider;
import emergencylanding.k.library.internalstate.Entity;
import emergencylanding.k.library.lwjgl.DisplayLayer;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.lwjgl.render.StringRenderer;
import emergencylanding.k.library.lwjgl.tex.ColorTexture;
import emergencylanding.k.library.main.KMain;
import emergencylanding.k.library.util.DrawableUtils;

public class TestingGuis extends KMain {
	private static Thread is;
	private static boolean run = true;
	private static final int TICKS_PER_SECOND = 120;
	private static final int DISPLAY_FPS_INDEX = 0, IS_INDEX = FPS.genIndex();
	private static TestGui g;
	private static GuiElement slider;

	public static void main(String[] args) {
		try {
			DisplayLayer.initDisplay(false, 800, 500, "Testing CrashCourse",
					true, args);
			FPS.enable(TestingGuis.DISPLAY_FPS_INDEX);
			TestingGuis.startISThreads();
			System.err.println("percent of 656/1000 = "
					+ Slider.getPercentClosestToValue(656, 0, 1000));
			while (TestingGuis.run) {
				TestingGuis.run = !Display.isCloseRequested();
				DisplayLayer.loop(120);
			}
			DisplayLayer.destroy();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void startISThreads() {
		Runnable isr = new Runnable() {
			Sync s = new Sync();

			@Override
			public void run() {
				FPS.init(TestingGuis.IS_INDEX);
				while (TestingGuis.run) {
					s.sync(TestingGuis.TICKS_PER_SECOND);
					int delta = FPS.update(TestingGuis.IS_INDEX);
					DisplayLayer.readDevices();
					TestingGuis.g.update();
				}
			}
		};
		TestingGuis.is = new Thread(isr);
		TestingGuis.is.setName("Internal State Thread");
		TestingGuis.is.start();
		System.err.println("ISThreads running!");
	}

	private String[] strvalues = new String[101];
	{
		strvalues[Slider.getPercentClosestToValue(0, 0, 1000)] = "Minimum";
		strvalues[Slider.getPercentClosestToValue(1000, 0, 1000)] = "Maximum";
	}

	@Override
	public void onDisplayUpdate(int delta) {
		TestingGuis.g.draw();
	}

	@Override
	public void init(String[] args) {
		TestingGuis.g = new TestGui();
		TestingGuis.slider = new Slider(50, 50, 0, 1000,
				DrawableUtils.scaledTexture(ColorTexture.RED, 10, 50),
				DrawableUtils.scaledTexture(ColorTexture.GREEN, 100, 50),
				"Test: ", strvalues, new StringRenderer(new Font(
						"times new roman", Font.PLAIN, 16), false));
		g.addElement(slider);
	}

	@Override
	public void registerRenders(
			HashMap<Class<? extends Entity>, Render<? extends Entity>> classToRender) {
		// TODO Auto-generated method stub
		
	}
}
