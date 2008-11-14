package sneer.pulp.reactive.signalchooser;

import sneer.kernel.container.Brick;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;

public interface SignalChooserManagerFactory extends Brick{
	
	<T> SignalChooserManager<T> newManager(ListSignal<T> input, SignalChooser<T> chooser, Omnivore<T> elementContentChangedReceiver);
}
