package sneer.pulp.reactive.signalchooser.impl;

import sneer.pulp.reactive.signalchooser.ElementsObserverFactory;

class ElementsObserverFactoryImpl implements ElementsObserverFactory {

	@Override
	public <T> ElementsObserver<T> newObserver(SignalChooser<T> chooser,	ElementListener<T> listener) {
		return new SignalsObserver<T>(chooser, listener);
	}
}
