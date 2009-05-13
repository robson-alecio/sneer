package sneer.pulp.reactive.signalchooser.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.collections.CollectionChange;
import sneer.pulp.reactive.collections.CollectionSignal;
import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import wheel.reactive.impl.EventReceiver;

class SignalChooserManagerImpl<T> {
	
	@SuppressWarnings("unused")	private final Object _refToAvoidGc;

	private final Map<T, ElementReceiver> _receiversByElement = new HashMap<T, ElementReceiver>();
	private final ListOfSignalsReceiver<T> _listOfSignalsReceiver;
	private final Object _monitor = new Object();
	
	public SignalChooserManagerImpl(CollectionSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver) {
		_listOfSignalsReceiver = listOfSignalsReceiver;
		_refToAvoidGc = new InputReceiver(input);
	}
	
	private void elementRemoved(T element) {
		if (signalChooser() == null) return;
		_receiversByElement.remove(element);
	}

	private void elementAdded(T element) {
		if (signalChooser() == null) return;
			
		ElementReceiver receiver = new ElementReceiver(element);
		_receiversByElement.put(element, receiver);
			
		receiver._isActive = true;
	}
	
	private SignalChooser<T> signalChooser() {
		return _listOfSignalsReceiver.signalChooser();
	}
	
	private class ElementReceiver extends EventReceiver<Object> {
		private final T _element;
		private volatile boolean _isActive;

		ElementReceiver(T element) {
			super(signalChooser().signalsToReceiveFrom(element));
			_element = element;
		}

		@Override
		public void consume(Object ignored) {
			if (!_isActive) return;
			_listOfSignalsReceiver.elementSignalChanged( _element);
		}
	}
	
	private class InputReceiver implements Consumer<CollectionChange<T>> {

		private final CollectionSignal<T> _input;

		public InputReceiver(CollectionSignal<T> input) {
			_input = input;
			_input.addReceiver(this);
			
			synchronized (_monitor) {
				for (T element : _input)
					SignalChooserManagerImpl.this.elementAdded(element);
			}
		}

		@Override
		synchronized public void consume(CollectionChange<T> change) {
			for(T element: change.elementsRemoved())
				SignalChooserManagerImpl.this.elementRemoved(element);
			
			for(T element: change.elementsAdded())
				SignalChooserManagerImpl.this.elementAdded(element);
		}
		
	}
}