package sneer.bricks.hardware.gui.guithread;

import sneer.foundation.brickness.Brick;
import sneer.foundation.commons.environments.*;

@Brick
public interface GuiThread {

	void strictInvokeAndWait(Runnable runnable);
	void strictInvokeLater(Runnable runnable);

	void invokeAndWait(Runnable runnable);
	void invokeLater(Runnable runnable);

	void assertInGuiThread();
	void strictInvokeAndWait(Environment environment, Runnable runnable);

}
