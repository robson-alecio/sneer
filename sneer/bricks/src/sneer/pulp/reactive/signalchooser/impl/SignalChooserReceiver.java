package sneer.pulp.reactive.signalchooser.impl;

import static sneer.commons.environments.Environments.my;

import java.util.HashMap;
import java.util.Map;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signals;
import sneer.pulp.reactive.collections.CollectionChange;
import sneer.pulp.reactive.collections.CollectionSignal;
import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChooser;

class SignalChooserReceiver<T> {
	
	@SuppressWarnings("unused")	private final Object _refToAvoidGc;

	private final Map<T, ElementReceiver> _receiversByElement = new HashMap<T, ElementReceiver>();
	private final ListOfSignalsReceiver<T> _listOfSignalsReceiver;
	private final Object _monitor = new Object();
	
	public SignalChooserReceiver(CollectionSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver) {
		_listOfSignalsReceiver = listOfSignalsReceiver;
		_refToAvoidGc = new InputReceiver(input);
	}
	
	private void elementRemoved(T element) {
		if (signalChooser() == null) return;
		ElementReceiver receiver = _receiversByElement.remove(element);
		receiver._isActive = false; //Refactor: Dispose the reception instead of setting false.
	}

	private void elementAdded(T element) {
		if (signalChooser() == null) return;
			
		ElementReceiver receiver = new ElementReceiver(element);
		_receiversByElement.put(element, receiver);
	}
	
	private SignalChooser<T> signalChooser() {
		return _listOfSignalsReceiver.signalChooser();
	}
	
	private class ElementReceiver {
		private boolean _isActive = false;

		ElementReceiver(final T element) {
			my(Signals.class).receive(this, new Consumer<Object>(){ @Override public void consume(Object value) {
				if (!_isActive) return;
				_listOfSignalsReceiver.elementSignalChanged(element);
			}}, signalChooser().signalsToReceiveFrom(element));
			
			startNotifyingReceiver();
		}

		private void startNotifyingReceiver() {
			_isActive = true;
		}

	}
	
	private class InputReceiver implements Consumer<CollectionChange<T>> {

		private final CollectionSignal<T> _input;

		public InputReceiver(CollectionSignal<T> input) {
			_input = input;
			_input.addReceiver(this);
			
			synchronized (_monitor) {
				for (T element : _input)
					SignalChooserReceiver.this.elementAdded(element);
			}
		}

		@Override
		synchronized public void consume(CollectionChange<T> change) {
			for(T element: change.elementsRemoved())
				SignalChooserReceiver.this.elementRemoved(element);
			
			for(T element: change.elementsAdded())
				SignalChooserReceiver.this.elementAdded(element);
		}
		
	}
}