package sneer.bricks.skin.widgets.reactive.autoscroll.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Ignore;

import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.bricks.skin.widgets.autoscroll.tests.AutoScrollTest;
import sneer.bricks.skin.widgets.reactive.autoscroll.ReactiveAutoScroll;
import sneer.foundation.lang.Consumer;

@Ignore
public class ReactiveAutoScrollTest extends AutoScrollTest {	
	
	@Override
	protected void autoScrollWithEventInsideGuiThread() throws Exception {
		_subject1 = my(ReactiveAutoScroll.class).create(_register.output(), new Consumer<String>(){ @Override public void consume(final String change) {
			my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
				append(_field1, change);
			}});
		}});
	}

	@Override
	protected void autoScrollWithEventOutsideGuiThread() throws Exception {
		_subject2 = my(ReactiveAutoScroll.class).create(_register.output(), new Consumer<String>(){@Override public void consume(final String change) {
			append(_field1, change);
		}});
	}
}