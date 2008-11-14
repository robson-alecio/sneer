package sneer.pulp.reactive.signalchooser.impl;

import sneer.pulp.reactive.signalchooser.ElementListener;
import sneer.pulp.reactive.signalchooser.SignalChooserManager;
import sneer.pulp.reactive.signalchooser.SignalChooserManagerFactory;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import wheel.reactive.lists.ListSignal;

class SignalChooserManagerFactoryImpl implements SignalChooserManagerFactory {

	@Override
	public <T> SignalChooserManager<T> newManager(ListSignal<T> input, SignalChooser<T> chooser, ElementListener<T> contentListener) {
		return new SignalChooserManagerImpl<T>(input, chooser, contentListener);
	}
}
