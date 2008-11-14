package sneer.pulp.reactive.signalchooser.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.pulp.reactive.signalchooser.SignalChooserManager;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.VisitingListReceiver;

class SignalChooserManagerImpl<T> implements SignalChooserManager<T>{
	
	private final SignalChooser<T> _chooser;
	private final Map<T, Receiver<Object>> _elementReceiversToAvoidGc = new HashMap<T, Receiver<Object>>();
	
	private final Omnivore<T> _elementContentChangedReceiver;
	
	@SuppressWarnings("unused")
	private ElementVisitingListReceiver _elementVisitingListReceiverToAvoidGc;
	
	SignalChooserManagerImpl(ListSignal<T> input, SignalChooser<T> chooser, Omnivore<T> elementContentChangedReceiver) {
		_chooser = chooser;
		_elementContentChangedReceiver = elementContentChangedReceiver;
		_elementVisitingListReceiverToAvoidGc = new ElementVisitingListReceiver(input);
	}
	
	public void elementRemoved(T element) {
		synchronized (_elementReceiversToAvoidGc) {
			if (_chooser == null) return;
			if (!_elementReceiversToAvoidGc.containsKey(element) ) return;
			_elementReceiversToAvoidGc.remove(element).removeFromSignals();
		}
	}

	public void elementAdded(T element) {
		synchronized (_elementReceiversToAvoidGc) {
			if (_chooser == null) return;
			
			ElementReceiver receiver = new ElementReceiver(element);
			_elementReceiversToAvoidGc.put(element, receiver);
			
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
			_elementContentChangedReceiver.consume(_element);
		}
	}
	
	private class ElementVisitingListReceiver extends VisitingListReceiver<T>{
		public ElementVisitingListReceiver(ListSignal<T> input) {
			super(input);
			for (T element : _input)
				SignalChooserManagerImpl.this.elementAdded(element);
		}
		
		@Override public void elementMoved(int oldIndex, int newIndex, T element) { /*ignore*/}
		@Override public void elementAdded(int index, T element)   {	 SignalChooserManagerImpl.this.elementAdded(element); }
		@Override public void elementInserted(int index, T element) { SignalChooserManagerImpl.this.elementAdded(element);	}
		@Override public void elementRemoved(int index, T element) { SignalChooserManagerImpl.this.elementRemoved(element); }

		@Override
		public void elementReplaced(int index, T oldElement, T newElement) {
			SignalChooserManagerImpl.this.elementRemoved(oldElement);
			SignalChooserManagerImpl.this.elementAdded(newElement);
		}
	}
}