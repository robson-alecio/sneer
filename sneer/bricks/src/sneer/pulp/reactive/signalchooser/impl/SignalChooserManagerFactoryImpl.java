package sneer.pulp.reactive.signalchooser.impl;

import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChooserManager;
import sneer.pulp.reactive.signalchooser.SignalChooserManagerFactory;

class SignalChooserManagerFactoryImpl implements SignalChooserManagerFactory {

	@Override
	public <T> SignalChooserManager<T> newManager(ListSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver) {
		return new SignalChooserManagerImpl<T>(input, listOfSignalsReceiver);
	}
}
