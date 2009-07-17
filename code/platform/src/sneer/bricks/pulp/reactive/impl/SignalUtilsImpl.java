package sneer.bricks.pulp.reactive.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.pulp.reactive.Reception;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.SignalUtils;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;

class SignalUtilsImpl implements SignalUtils {

	@Override
	public <T> void waitForValue(Signal<T> signal, T expectedValue) {
		while (true) {
			if (expectedValue == null && signal.currentValue() == null) return;
			if (expectedValue != null && expectedValue.equals(signal.currentValue())) return;

			my(Threads.class).sleepWithoutInterruptions(10); //Optimize
		}
	}

	@Override
	public <T> void waitForElement(SetSignal<T> setSignal, T expected) {
		while (true) {
			if (setSignal.currentContains(expected)) return;
			my(Threads.class).sleepWithoutInterruptions(10); //Optimize
		}		
	}

	@Override
	public <T> void waitForElement(SetSignal<T> setSignal, final Predicate<T> predicate) {
		final Latch latch = my(Threads.class).newLatch();
		@SuppressWarnings("unused")
		Reception reception = my(Signals.class).receive(setSignal, new Consumer<CollectionChange<T>>() { @Override public void consume(CollectionChange<T> change) {
			for (T element : change.elementsAdded())
				if (predicate.evaluate(element)) latch.open();
		}});
		
		latch.await();
	}	
}
