package sneer.bricks.pulp.reactive;

import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.foundation.brickness.Brick;

@Brick
public interface SignalUtils {

	void waitForValue(Object expectedValue, Signal<?> signal);

	<T> void waitForElement(T expected, SetSignal<T> setSignal);

}