package sneer.pulp.reactive.signalchooser.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.collections.CollectionChange;
import sneer.pulp.reactive.collections.CollectionSignal;
import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import wheel.reactive.impl.EventReceiver;

class SignalChooserManagerImpl<T> {
	
	@SuppressWarnings("unused")	private final Object _refToAvoidGc;

	private final List<ElementReceiver> _elementReceivers = new ArrayList<ElementReceiver>();
	private final ListOfSignalsReceiver<T> _listOfSignalsReceiver;
	private final Object _monitor = new Object();
	
	public SignalChooserManagerImpl(CollectionSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver) {
		_listOfSignalsReceiver = listOfSignalsReceiver;
		_refToAvoidGc = new InputReceiver(input);
	}
	
	private ElementReceiver findFirstReceiver(T element) { //Fix Remove this method after bugfix (run SortTest to see the bug)
		for(ElementReceiver receiver : _elementReceivers)
			if(element==receiver._element)
				return receiver;

		throw new IllegalStateException("Not found any ElementReceiver for element: " + element);
	}

	private void elementRemoved(T element) {
		synchronized (_monitor) {
			if (signalChooser() == null) return;
			_elementReceivers.remove(findFirstReceiver(element));
		}
	}

	private void elementAdded(T element) {
		synchronized (_monitor) {
			if (signalChooser() == null) return;
			
			ElementReceiver receiver = new ElementReceiver(element);
			_elementReceivers.add(receiver);
			
			receiver._isActive = true;
		}
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
			synchronized (_monitor) {
				//Refactor: The line below can be deleted. Is the synchronized block still necessary with the line removed?
				//int index = _elementReceivers.indexOf(this);
				_listOfSignalsReceiver.elementSignalChanged( _element);
			}
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
		public void consume(CollectionChange<T> change) {
			for(T element: change.elementsRemoved())
				SignalChooserManagerImpl.this.elementRemoved(element);
			
			for(T element: change.elementsAdded())
				SignalChooserManagerImpl.this.elementAdded(element);
		}
		
	}
}