package sneer.pulp.reactive;

import sneer.brickness.Brick;
import sneer.pulp.reactive.collections.SetSignal;

@Brick
public interface SignalUtils {

	void waitForValue(Object expectedValue, Signal<?> signal);

	<T> void waitForElement(T expected, SetSignal<T> setSignal);

}