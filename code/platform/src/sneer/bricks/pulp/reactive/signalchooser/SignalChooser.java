/**
 * 
 */
package sneer.bricks.pulp.reactive.signalchooser;

import sneer.bricks.pulp.reactive.Signal;

public interface SignalChooser<T> {
	Signal<?>[] signalsToReceiveFrom(T element);
}