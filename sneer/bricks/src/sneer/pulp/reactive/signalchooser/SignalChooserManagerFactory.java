package sneer.pulp.reactive.signalchooser;

import sneer.brickness.Brick;
import sneer.pulp.reactive.collections.ListSignal;

@Brick
public interface SignalChooserManagerFactory {
	
	<T> SignalChooserManager<T> newManager(ListSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver);
}
