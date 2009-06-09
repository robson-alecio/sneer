package sneer.bricks.pulp.reactive.signalchooser.impl;

import sneer.bricks.pulp.reactive.collections.CollectionSignal;
import sneer.bricks.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.bricks.pulp.reactive.signalchooser.SignalChoosers;

class SignalChoosersImpl implements SignalChoosers {

	@Override
	public <T> Object receive(CollectionSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver) {
		return new SignalChooserReceiver<T>(input, listOfSignalsReceiver);
	}
}
