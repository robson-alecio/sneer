package sneer.bricks.pulp.reactive.signalchooser.impl;

import static sneer.foundation.commons.environments.Environments.my;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.hardware.cpu.lang.Consumer;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.pulp.reactive.collections.CollectionChange;
import sneer.bricks.pulp.reactive.collections.CollectionSignal;
import sneer.bricks.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.bricks.pulp.reactive.signalchooser.SignalChooser;
import sneer.foundation.commons.lang.exceptions.NotImplementedYet;

class SignalChooserReceiver<T> {
	
	@SuppressWarnings("unused")	private final Object _refToAvoidGc;

	private final Map<T, ElementReceiver> _receiversByElement = new HashMap<T, ElementReceiver>();
	private final ListOfSignalsReceiver<T> _listOfSignalsReceiver;
	
	public SignalChooserReceiver(CollectionSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver) {
		_listOfSignalsReceiver = listOfSignalsReceiver;
		_refToAvoidGc = new InputReceiver(input);
	}
	
	private void elementRemoved(T element) {
		if (signalChooser() == null) return;
		ElementReceiver receiver = _receiversByElement.remove(element);
		if (receiver == null) return;
		receiver._isActive = false; //Refactor: Dispose the reception (if supported) instead of setting false.
	}

	private void elementAdded(T element) {
		if (signalChooser() == null) return;
			
		ElementReceiver receiver = new ElementReceiver(element);
		if (_receiversByElement.put(element, receiver) != null)
			throw new NotImplementedYet("Duplicated elements are not supported since there is no tracking yet of the number of occurences.");
	}
	
	private SignalChooser<T> signalChooser() {
		return _listOfSignalsReceiver.signalChooser();
	}
	
	private class ElementReceiver {
		private boolean _isActive = false;
		@SuppressWarnings("unused") private final Object _referenceToAvoidGc;

		ElementReceiver(final T element) {
			_referenceToAvoidGc = my(Signals.class).receive(new Consumer<Object>(){ @Override public void consume(Object value) {
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
			
			synchronized (this) {
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