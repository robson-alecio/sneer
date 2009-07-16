package sneer.bricks.pulp.reactive;

import sneer.bricks.pulp.reactive.collections.SetSignal;
import sneer.foundation.brickness.Brick;

@Brick
public interface SignalUtils {

	void waitForValue(Signal<?> signal, Object expectedValue);

	<T> void waitForElement(SetSignal<T> setSignal, T expected);

}