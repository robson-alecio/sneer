/**
 * 
 */
package sneer.pulp.reactive.signalchooser;

import sneer.pulp.reactive.Signal;

public interface SignalChooser<T> {
	Signal<?>[] signalsToReceiveFrom(T element);
}