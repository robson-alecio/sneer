package sneer.pulp.reactive.signalchooser.impl;

import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChooserManager;
import sneer.pulp.reactive.signalchooser.SignalChooserManagerFactory;
import wheel.reactive.lists.ListSignal;

class SignalChooserManagerFactoryImpl implements SignalChooserManagerFactory {

	@Override
	public <T> SignalChooserManager<T> newManager(ListSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver) {
		return new SignalChooserManagerImpl<T>(input, listOfSignalsReceiver);
	}
}
