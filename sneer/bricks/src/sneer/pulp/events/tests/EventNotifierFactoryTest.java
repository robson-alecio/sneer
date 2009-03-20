package sneer.pulp.events.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.pulp.events.EventNotifierFactory;
import sneer.pulp.reactive.Signals;
import wheel.lang.Consumer;

public class EventNotifierFactoryTest extends BrickTest {
	
	@Test (expected = Throwable.class)
	public void throwablesBubbleUpDuringTests() {
		my(EventNotifierFactory.class).create(new Consumer<Consumer<? super Object>>() { @Override public void consume(Consumer<Object> receiver) {
			throw new Error();
		}}).output().addReceiver(my(Signals.class).sink());
	}

}
