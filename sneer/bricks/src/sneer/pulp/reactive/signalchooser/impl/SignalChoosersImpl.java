package sneer.pulp.reactive.signalchooser.impl;

import sneer.pulp.reactive.collections.ListSignal;
import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChoosers;

class SignalChoosersImpl implements SignalChoosers {

	@Override
	public <T> Object newManager(ListSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver) {
		return new SignalChooserManagerImpl<T>(input, listOfSignalsReceiver);
	}
}
