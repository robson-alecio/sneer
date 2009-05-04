package sneer.pulp.natures.gui;

import sneer.commons.environments.*;
import sneer.hardware.gui.guithread.*;
import static sneer.commons.environments.Environments.my;
public class GUINatureRuntime {

	public static void runInGuiThread(Environment environment, final Runnable runnable) {
		Environments.runWith(environment, new Runnable() { @Override public void run() {
			my(GuiThread.class).invokeAndWait(runnable);
		}});
	}
}
