package sneer.pulp.reactive.signalchooser.impl;

import java.util.ArrayList;
import java.util.List;

import sneer.pulp.reactive.signalchooser.ListOfSignalsReceiver;
import sneer.pulp.reactive.signalchooser.SignalChooser;
import sneer.pulp.reactive.signalchooser.SignalChooserManager;
import wheel.reactive.impl.EventReceiver;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.VisitingListReceiver;

class SignalChooserManagerImpl<T> implements SignalChooserManager<T>{
	
	@SuppressWarnings("unused")	private final Object _refToAvoidGc;

	private final List<ElementReceiver> _elementReceivers = new ArrayList<ElementReceiver>();
	private final ListOfSignalsReceiver<T> _listOfSignalsReceiver;
	private final Object _monitor = new Object();
	
	public SignalChooserManagerImpl(ListSignal<T> input, ListOfSignalsReceiver<T> listOfSignalsReceiver) {
		_listOfSignalsReceiver = listOfSignalsReceiver;
		_refToAvoidGc = new ElementVisitingListReceiver(input);
	}
	
	private void checkElementIndex(int index, T element) { //Fix Remove this method after bugfix (run SortTest to see the bug)
		ElementReceiver receiver = _elementReceivers.get(index);
		T actual = receiver._element;
		if(actual!=element) 
			throw new IllegalStateException("Wrong element index!! index:" + index + ", expected=" + element + ", actual=" + actual  );
	}

	public void elementRemoved(int index, T element) {
		synchronized (_monitor) {
			if (signalChooser() == null) return;
			checkElementIndex(index, element);
			_elementReceivers.remove(index);
		}
	}

	public void elementAdded(int index, T element) {
		synchronized (_monitor) {
			if (signalChooser() == null) return;
			
			ElementReceiver receiver = new ElementReceiver(element);
			_elementReceivers.add(index, receiver);
			checkElementIndex(index, element);
			
			receiver._isActive = true;
		}
	}

	public void elementMoved(int oldIndex, int newIndex, T element) {
		synchronized (_monitor) {
			if(oldIndex==newIndex) return;
			int tmpIndex = newIndex>oldIndex ? newIndex-1 : newIndex;
			tmpIndex = newIndex<0 ? 0 : newIndex;
			
			ElementReceiver receiver = _elementReceivers.get(oldIndex);
			checkElementIndex(oldIndex, element);
			_elementReceivers.remove(oldIndex);

			int size = _elementReceivers.size();
			if(tmpIndex > size){
				_elementReceivers.add(receiver);
				tmpIndex = size;
			}else
				_elementReceivers.add(tmpIndex, receiver);
			checkElementIndex(tmpIndex, element);
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
				int index = _elementReceivers.indexOf(this);
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