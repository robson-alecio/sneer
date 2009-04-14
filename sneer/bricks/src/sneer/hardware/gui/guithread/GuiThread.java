package sneer.hardware.gui.guithread;

import sneer.brickness.Brick;

@Brick
public interface GuiThread {

	void strictInvokeAndWait(Runnable runnable);
	void strictInvokeLater(Runnable runnable);

	void invokeAndWait(Runnable runnable);
	void invokeLater(Runnable runnable);

	void assertInGuiThread();

}
