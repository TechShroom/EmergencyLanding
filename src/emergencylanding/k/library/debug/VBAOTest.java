package emergencylanding.k.library.debug;

import java.util.HashMap;

import org.lwjgl.opengl.Display;

import emergencylanding.k.imported.Sync.RunningAvg;
import emergencylanding.k.library.internalstate.Entity;
import emergencylanding.k.library.lwjgl.DisplayLayer;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.lwjgl.render.VBAO;
import emergencylanding.k.library.lwjgl.render.VertexData;
import emergencylanding.k.library.main.KMain;



public class VBAOTest extends KMain {
	VBAO quad = null;
	RunningAvg davg = new RunningAvg(10);

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		DisplayLayer.initDisplay(false, 1000, 600, "VBAO NewElTest", false,
				false, args);
		FPS.enable(0);
		while (!Display.isCloseRequested()) {
			DisplayLayer.loop(12000);
		}
		DisplayLayer.destroy();
	}

	@Override
	public void onDisplayUpdate(int delta) {
		davg.add(delta * 100);
		delta = (int) davg.avg();
		delta /= 10;
		System.err.println(delta);
		float[] v1 = {0, 100, 0, 1, 1, 1};
		float[] v2 = {0, 0, 0, 1, 1, 1};
		float[] v3 = {100, 0, 0, 1, 1, 1};
		float[] v4 = {100, 100, 0, 1, 1, 1};
		int order = VertexData.COLOR_FIRST;
		VertexData[] verts = {new VertexData(order, v1),
				new VertexData(order, v2), new VertexData(order, v3),
				new VertexData(order, v4)};
		byte[] indexControl = {
				// t1
				0, 1, 2,
				// t2
				2, 3, 0};
		quad = new VBAO(verts, indexControl);
		quad.draw();
		quad.destroy();
	}

	@Override
	public void init(String[] args) {
		float[] v1 = {50, 200, 0, 1f, 0, 0};
		float[] v2 = {50, 50, 0, 0, 1f, 0f};
		float[] v3 = {200, 50, 0, 0, 0f, 1f};
		float[] v4 = {200, 200, 0, 1f, 1f, 1f};
		int order = VertexData.COLOR_FIRST;
		VertexData[] verts = {new VertexData(order, v1),
				new VertexData(order, v2), new VertexData(order, v3),
				new VertexData(order, v4)};
		byte[] indexControl = {
				// t1
				0, 1, 2,
				// t2
				2, 3, 0};
		quad = new VBAO(verts, indexControl);
	}

	@Override
	public void registerRenders(
			HashMap<Class<? extends Entity>, Render<? extends Entity>> classToRender) {
	}

}
