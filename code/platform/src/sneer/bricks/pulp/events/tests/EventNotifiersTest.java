package sneer.bricks.pulp.events.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.pulp.events.EventNotifier;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.Consumer;

public class EventNotifiersTest extends BrickTest {
	
	@Test (expected = Throwable.class)
	public void throwablesBubbleUpDuringTests() {
		Consumer<Consumer<? super Object>> receiverHandler = new Consumer<Consumer<? super Object>>() { @Override public void consume(Consumer<Object> receiver) {
			throw new Error();
		}};
		EventNotifier<Object> notifier = my(EventNotifiers.class).newInstance(receiverHandler);
		notifier.output().addReceiver(my(Signals.class).sink());
	}
	
	@Test (timeout = 2000)
	public void actsAsPulser() {
		final EventNotifier<Object> notifier = my(EventNotifiers.class).newInstance();
		final Latch pulseLatch = my(Threads.class).newLatch();
		@SuppressWarnings("unused")
		final Contract pulseContract = notifier.output().addPulseReceiver(pulseLatch);
		
		notifier.notifyReceivers("foo");
		
		pulseLatch.waitTillOpen();
	}

}
