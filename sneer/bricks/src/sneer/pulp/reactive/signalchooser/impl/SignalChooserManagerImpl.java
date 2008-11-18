package sneer.pulp.reactive.signalchooser.impl;

import java.util.ArrayList;
import java.util.List;

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

	private final List<ElementReceiver> _elementReceiversToAvoidGc = new ArrayList<ElementReceiver>();
	private final ListOfSignalsReceiver<T> _listOfSignalsReceiver;
	private final Object _monitor = new Object();
	
	public SignalChooserManagerImpl(ListSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver) {
		_listOfSignalsReceiver = listOfSignalsReceiver;
		_elementVisitingListReceiverToAvoidGc = new ElementVisitingListReceiver(input);
	}
	
	private void checkElementIndex(int index, T element) { //Fix Remove this method after bugfix (run SortTest to see the bug)
		ElementReceiver receiver = _elementReceiversToAvoidGc.get(index);
		T actual = receiver._element;
		if(actual!=element) 
			throw new IllegalStateException("Wrong element index!! index:" + index + ", expected=" + element + ", actual=" + actual  );
	}

	public void elementRemoved(int index, T element) {
		synchronized (_monitor) {
			if (signalChooser() == null) return;
			checkElementIndex(index, element);
			_elementReceiversToAvoidGc.remove(index);
		}
	}

	public void elementAdded(int index, T element) {
		synchronized (_monitor) {
			if (signalChooser() == null) return;
			
			ElementReceiver receiver = new ElementReceiver(element);
			_elementReceiversToAvoidGc.add(index, receiver);
			checkElementIndex(index, element);
			
			for (Signal<?> signal : signalChooser().signalsToReceiveFrom(element))
				receiver.addToSignal(signal);
			
			receiver._isActive = true;
		}
	}

	public void elementMoved(int oldIndex, int newIndex, T element) {
		synchronized (_monitor) {
			if(oldIndex==newIndex) return;
			int tmpIndex = newIndex>oldIndex ? newIndex-1 : newIndex;
			tmpIndex = newIndex<0 ? 0 : newIndex;
			
			ElementReceiver receiver = _elementReceiversToAvoidGc.get(oldIndex);
			checkElementIndex(oldIndex, element);
			_elementReceiversToAvoidGc.remove(oldIndex);

			int size = _elementReceiversToAvoidGc.size();
			if(tmpIndex > size){
				_elementReceiversToAvoidGc.add(receiver);
				tmpIndex = size;
			}else
				_elementReceiversToAvoidGc.add(tmpIndex, receiver);
			checkElementIndex(tmpIndex, element);
		}
	}
	
	private SignalChooser<T> signalChooser() {
		return _listOfSignalsReceiver.signalChooser();
	}
	
	private class ElementReceiver extends Receiver<Object> {
		private final T _element;
		private volatile boolean _isActive;

		ElementReceiver(T element) {
			_element = element;
		}

		@Override
		public void consume(Object ignored) {
			if (!_isActive) return;
			synchronized (_monitor) {
				int index = _elementReceiversToAvoidGc.indexOf(this);
				_listOfSignalsReceiver.elementSignalChanged( index,  _element);
			}
		}
	}
	
	private class ElementVisitingListReceiver extends VisitingListReceiver<T>{
		public ElementVisitingListReceiver(ListSignal<T> input) {
			super(input);
			synchronized (_monitor) {
				int index = 0;
				for (T element : _input)
					SignalChooserManagerImpl.this.elementAdded(index++, element);
			}
		}
		
		@Override public void elementMoved(int oldIndex, int newIndex, T element) {SignalChooserManagerImpl.this.elementMoved(oldIndex, newIndex, element);}
		@Override public void elementAdded(int index, T element)   {	 SignalChooserManagerImpl.this.elementAdded(index, element); }
		@Override public void elementInserted(int index, T element) { SignalChooserManagerImpl.this.elementAdded(index, element);	}
		@Override public void elementRemoved(int index, T element) { SignalChooserManagerImpl.this.elementRemoved(index, element); }

		@Override
		public void elementReplaced(int index, T oldElement, T newElement) {
			SignalChooserManagerImpl.this.elementRemoved(index, oldElement);
			SignalChooserManagerImpl.this.elementAdded(index, newElement);
		}
	}
}