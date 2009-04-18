package sneer.pulp.events.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.events.EventNotifiers;
import sneer.pulp.reactive.Signals;

public class EventNotifierFactoryTest extends BrickTest {
	
	@Test (expected = Throwable.class)
	public void throwablesBubbleUpDuringTests() {
		my(EventNotifiers.class).create(new Consumer<Consumer<? super Object>>() { @Override public void consume(Consumer<Object> receiver) {
			throw new Error();
		}}).output().addReceiver(my(Signals.class).sink());
	}

}
