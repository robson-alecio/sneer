package sneer.bricks.pulp.events.tests;

import static sneer.foundation.commons.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.events.EventNotifiers;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.brickness.testsupport.BrickTest;

public class EventNotifierFactoryTest extends BrickTest {
	
	@Test (expected = Throwable.class)
	public void throwablesBubbleUpDuringTests() {
		my(EventNotifiers.class).create(new Consumer<Consumer<? super Object>>() { @Override public void consume(Consumer<Object> receiver) {
			throw new Error();
		}}).output().addReceiver(my(Signals.class).sink());
	}

}
