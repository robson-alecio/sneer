package sneer.pulp.reactive.signalchooser.impl;

import sneer.pulp.reactive.collections.CollectionSignal;
import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChoosers;

class SignalChoosersImpl implements SignalChoosers {

	@Override
	public <T> Object receive(CollectionSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver) {
		return new SignalChooserReceiver<T>(input, listOfSignalsReceiver);
	}
}
