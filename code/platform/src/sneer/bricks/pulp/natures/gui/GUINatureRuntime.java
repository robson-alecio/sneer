package sneer.bricks.pulp.natures.gui;

import sneer.bricks.hardware.gui.guithread.*;
import sneer.foundation.environments.*;
import static sneer.foundation.environments.Environments.my;
public class GUINatureRuntime {

	public static void runInGuiThread(Environment environment, final Runnable runnable) {
		Environments.runWith(environment, new Runnable() { @Override public void run() {
			my(GuiThread.class).invokeAndWaitForWussies(runnable);
		}});
	}
}
