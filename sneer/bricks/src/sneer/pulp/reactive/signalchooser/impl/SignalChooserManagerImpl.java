package sneer.pulp.reactive.signalchooser.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.pulp.reactive.signalchooser.SignalChooserManager;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.VisitingListReceiver;

class SignalChooserManagerImpl<T> implements SignalChooserManager<T>{
	
	@SuppressWarnings("unused")
	private ElementVisitingListReceiver _elementVisitingListReceiverToAvoidGc;

	private final Map<T, Receiver<Object>> _elementReceiversToAvoidGc = new HashMap<T, Receiver<Object>>();
	private final ListOfSignalsReceiver<T> _listOfSignalsReceiver;
	
	public SignalChooserManagerImpl(ListSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver) {
		_listOfSignalsReceiver = listOfSignalsReceiver;
		_elementVisitingListReceiverToAvoidGc = new ElementVisitingListReceiver(input);
	}

	public void elementRemoved(T element) {
		synchronized (_elementReceiversToAvoidGc) {
			if (signalChooser() == null) return;
			if (!_elementReceiversToAvoidGc.containsKey(element) ) return;
			_elementReceiversToAvoidGc.remove(element).removeFromSignals();
		}
	}

	public void elementAdded(int index, T element) {
		synchronized (_elementReceiversToAvoidGc) {
			if (signalChooser() == null) return;
			
			ElementReceiver receiver = new ElementReceiver(index, element);
			_elementReceiversToAvoidGc.put(element, receiver);
			
			for (Signal<?> signal : signalChooser().signalsToReceiveFrom(element))
				receiver.addToSignal(signal);
			
			receiver._isActive = true;
		}
	}

	private SignalChooser<T> signalChooser() {
		return _listOfSignalsReceiver.signalChooser();
	}
	
	private class ElementReceiver extends Receiver<Object> {
		private final T _element;
		private final int _index;
		private volatile boolean _isActive;

		ElementReceiver( int index, T element) {
			_index = index;
			_element = element;
		}

		@Override
		public void consume(Object ignored) {
			if (!_isActive) return;
			_listOfSignalsReceiver.elementSignalChanged(_index, _element);
		}
	}
	
	private class ElementVisitingListReceiver extends VisitingListReceiver<T>{
		public ElementVisitingListReceiver(ListSignal<T> input) {
			super(input);
			int index = 0;
			for (T element : _input)
				SignalChooserManagerImpl.this.elementAdded(index++, element);
		}
		
		@Override public void elementMoved(int oldIndex, int newIndex, T element) { /*ignore*/}
		@Override public void elementAdded(int index, T element)   {	 SignalChooserManagerImpl.this.elementAdded(index, element); }
		@Override public void elementInserted(int index, T element) { SignalChooserManagerImpl.this.elementAdded(index, element);	}
		@Override public void elementRemoved(int index, T element) { SignalChooserManagerImpl.this.elementRemoved(element); }

		@Override
		public void elementReplaced(int index, T oldElement, T newElement) {
			SignalChooserManagerImpl.this.elementRemoved(oldElement);
			SignalChooserManagerImpl.this.elementAdded(index, newElement);
		}
	}
}