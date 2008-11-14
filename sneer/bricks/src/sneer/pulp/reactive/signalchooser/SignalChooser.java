/**
 * 
 */
package sneer.pulp.reactive.signalchooser;

import wheel.reactive.Signal;

public interface SignalChooser<T> {
	Signal<?>[] signalsToReceiveFrom(T element);
}