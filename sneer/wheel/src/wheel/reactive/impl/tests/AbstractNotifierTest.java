package wheel.reactive.impl.tests;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.brickness.testsupport.BrickTestRunner;
import wheel.lang.Consumer;
import wheel.reactive.Signals;
import wheel.reactive.impl.AbstractNotifier;

@RunWith(BrickTestRunner.class)
public class AbstractNotifierTest {
	
	@Ignore
	@Test (expected = Error.class)
	public void throwablesBubbleUpDuringTests() {
		new AbstractNotifier<Object>() {
			@Override
			protected void initReceiver(Consumer<Object> receiver) {
				throw new Error();
			}
		}.addReceiver(Signals.sink());
	}

}
