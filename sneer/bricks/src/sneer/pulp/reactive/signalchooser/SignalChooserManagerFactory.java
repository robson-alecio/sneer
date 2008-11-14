package sneer.pulp.reactive.signalchooser;

import sneer.kernel.container.Brick;
import wheel.reactive.lists.ListSignal;

public interface SignalChooserManagerFactory extends Brick{
	
	<T> SignalChooserManager<T> newManager(ListSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver);
}
