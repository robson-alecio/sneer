package sneer.bricks.pulp.reactive.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.cpu.lang.contract.Contract;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.SignalUtils;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;

class SignalUtilsImpl implements SignalUtils {

	@Override
	public <T> void waitForValue(Signal<T> signal, final T expected) {
		final Latch latch = my(Threads.class).newLatch();
		@SuppressWarnings("unused")
		Contract reception = my(Signals.class).receive(signal, new Consumer<T>() { @Override public void consume(T value) {
			if (equalsWithNulls(expected, value))
				latch.open();
		}});
		
		latch.waitTillOpen();
	}

	@Override
	public <T> void waitForElement(SetSignal<T> setSignal, final T expected) {
		waitForElement(setSignal, new Predicate<T>() { @Override public boolean evaluate(T value) {
			return equalsWithNulls(expected, value);
		}});		
	}
	
	@Override
	public <T> void waitForElement(SetSignal<T> setSignal, final Predicate<T> predicate) {
		final Latch latch = my(Threads.class).newLatch();
		@SuppressWarnings("unused")
		Contract reception = my(Signals.class).receive(setSignal, new Consumer<CollectionChange<T>>() { @Override public void consume(CollectionChange<T> change) {
			for (T element : change.elementsAdded())
				if (predicate.evaluate(element)) latch.open();
		}});
		
		latch.waitTillOpen();
	}
	
	static private <T> boolean equalsWithNulls(final T expected, T value) {
		return value == null
			? expected == null
			: value.equals(expected);
	}
	
}

