package emergencylanding.k.library.debug;

import java.awt.Rectangle;
import java.util.Random;

import k.core.util.Helper;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import emergencylanding.k.library.internalstate.Entity;
import emergencylanding.k.library.internalstate.world.World;
import emergencylanding.k.library.lwjgl.control.MouseHelp;
import emergencylanding.k.library.lwjgl.tex.ColorTexture;
import emergencylanding.k.library.lwjgl.tex.Texture;

public class TestEntity extends Entity {
	private Random rnd = new Random(
			(long) (Sys.getTime() / new Random().nextDouble()));
	boolean onScreenX = true, onScreenY = true;
	private static Texture[] texmix = {ColorTexture.BLUE, ColorTexture.RED,
			ColorTexture.GREEN, ColorTexture.PURPLE};

	public TestEntity() {
		super(0, 0, 0, TestEntity.texmix[0]);
	}

	@Override
	public void updateOnTick(float delta, World w) {
		if (pos.x > Display.getWidth() || pos.x < 0) {
			setXPos(!(pos.x > Display.getWidth()) ? Display.getWidth() : 0);
		}
		if (pos.y > Display.getHeight() || pos.y < 0) {
			setYPos(!(pos.y > Display.getHeight()) ? Display.getHeight() : 0);
		}
		if (Math.sqrt(vel.y * vel.y + vel.x * vel.x) < 0.5) {
			tex = Helper.Arrays.randomArray(TestEntity.texmix)[0];
		}
		super.updateOnTick(delta, w);
		checkForClick();
		doRandomVelocityChange();
	}

	private void checkForClick() {
		if (MouseHelp.clickedInRect(new Rectangle((int) pos.x, (int) pos.y, 10,
				10), MouseHelp.LMB)) {
			System.out.println("My color is "
					+ ((ColorTexture) tex).getRawColor().toString());
		}
	}

	private void doRandomVelocityChange() {
		if (rnd.nextFloat() < 0.9) {
			return;
		}
		if (vel.y < 10 && vel.y > 0) {
			vel.lastY = vel.y;
			vel.y += rnd.nextDouble();
			vel.y *= rnd.nextBoolean() ? -1 : 1;
		} else {
			vel.lastY = vel.y;
			vel.y = vel.y > 10 ? 10 : 0.1f;
		}
		if (vel.x < 10 && vel.x > 0) {
			vel.lastX = vel.x;
			vel.x += rnd.nextDouble();
			vel.x *= rnd.nextBoolean() ? -1 : 1;
		} else {
			vel.lastX = vel.x;
			vel.x = vel.x > 10 ? 10 : 0.1f;
		}
	}
}
