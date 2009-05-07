package sneer.pulp.reactive.signalchooser;

import sneer.brickness.Brick;
import sneer.pulp.reactive.collections.CollectionSignal;

@Brick
public interface SignalChoosers {
	
	<T> Object newManager(CollectionSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver);
}
