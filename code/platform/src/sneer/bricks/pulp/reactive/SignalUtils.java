package sneer.bricks.pulp.reactive;

import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Predicate;

@Brick
public interface SignalUtils {

	<T> void waitForValue(Signal<T> signal, T expectedValue);

	<T> void waitForElement(SetSignal<T> setSignal, T expected);

	<T> void waitForElement(SetSignal<T> setSignal,	Predicate<T> predicate);

}