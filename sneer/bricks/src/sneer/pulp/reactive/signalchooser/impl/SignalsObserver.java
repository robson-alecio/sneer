package sneer.pulp.reactive.signalchooser.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.pulp.reactive.signalchooser.ElementsObserverFactory.ElementListener;
import sneer.pulp.reactive.signalchooser.ElementsObserverFactory.ElementsObserver;
import sneer.pulp.reactive.signalchooser.ElementsObserverFactory.SignalChooser;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;

class SignalsObserver<T> implements ElementsObserver<T>{
	
	private final ElementListener<T> _contentListener;
	private final SignalChooser<T> _chooser;
	private final Map<T, Receiver<Object>> _elementReceivers = new HashMap<T, Receiver<Object>>();

	SignalsObserver(SignalChooser<T> chooser, ElementListener<T> contentListener) {
		_chooser = chooser;
		_contentListener = contentListener;
	}
	
	public void elementRemoved(T element) {
		synchronized (_elementReceivers) {
			if (_chooser == null) return;
			if (!_elementReceivers.containsKey(element) ) return;
			_elementReceivers.remove(element).removeFromSignals();
		}
	}

	public void elementAdded(T element) {
		synchronized (_elementReceivers) {
			if (_chooser == null) return;
			
			ElementReceiver receiver = new ElementReceiver(element);
			_elementReceivers.put(element, receiver);
			
			for (Signal<?> signal : _chooser.signalsToReceiveFrom(element))
				receiver.addToSignal(signal);
			
			receiver._isActive = true;
		}
	}
	
	private class ElementReceiver extends Receiver<Object> {
		
		private final T _element;
		private volatile boolean _isActive;

		ElementReceiver( T element) {
			_element = element;
		}

		@Override
		public void consume(Object ignored) {
			if (!_isActive) return;
			_contentListener.elementChanged(_element);
		}
	}
}