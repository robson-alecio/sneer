package sneer.pulp.reactive.signalchooser.impl;

import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.pulp.reactive.signalchooser.SignalChooserManager;
import sneer.pulp.reactive.signalchooser.SignalChooserManagerFactory;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;

class SignalChooserManagerFactoryImpl implements SignalChooserManagerFactory {

	@Override
	public <T> SignalChooserManager<T> newManager(ListSignal<T> input, SignalChooser<T> chooser, Omnivore<T> elementContentChangedReceiver) {
		return new SignalChooserManagerImpl<T>(input, chooser, elementContentChangedReceiver);
	}
}
