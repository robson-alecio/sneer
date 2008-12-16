package wheel.reactive.impl.tests;

import org.junit.Test;
import org.junit.runner.RunWith;

import wheel.lang.Consumer;
import wheel.reactive.Signals;
import wheel.reactive.impl.AbstractNotifier;
import wheel.testutil.WheelEnvironment;

@RunWith(WheelEnvironment.class)
public class AbstractNotifierTest {
	
	@Test (expected = Error.class)
	public void testThrowsBubbleUpDuringTests() {
		new AbstractNotifier<Object>() {
			@Override
			protected void initReceiver(Consumer<Object> receiver) {
				throw new Error();
			}
		}.addReceiver(Signals.sink());
	}

}
