package emergencylanding.k.library.main;

import java.util.HashMap;

import emergencylanding.k.library.internalstate.Entity;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.util.LUtils;
import emergencylanding.k.library.util.StackTraceInfo;

public abstract class KMain {
	private static KMain insts = null;
	private static Thread displayThread = null;

	public abstract void onDisplayUpdate(int delta);

	public abstract void init(String[] args);

	public static void setInst(KMain inst) {
		try {
			LUtils.checkAccessor("crashcourse.k.library.*",
					StackTraceInfo.getInvokingClassName());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		KMain.insts = inst;
	}

	public static KMain getInst() {
		return KMain.insts;
	}

	public static void setDisplayThread(Thread t) {
		try {
			LUtils.checkAccessor("crashcourse.k.library.*",
					StackTraceInfo.getInvokingClassName());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		KMain.displayThread = t;
		System.err.println("Dispaly thread set");
	}

	public static Thread getDisplayThread() {
		return KMain.displayThread;
	}

	public abstract void registerRenders(
			HashMap<Class<? extends Entity>, Render<? extends Entity>> classToRender);
}
