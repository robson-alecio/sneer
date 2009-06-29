package sneer.bricks.hardware.gui.guithread;

import sneer.foundation.brickness.Brick;

@Brick
public interface GuiThread {

	void invokeAndWait(Runnable runnable);
	void invokeLater(Runnable runnable);

	void invokeAndWaitForWussies(Runnable runnable);
	void invokeLaterForWussies(Runnable runnable);

	void assertInGuiThread();
	void assertNotInGuiThread();

}
