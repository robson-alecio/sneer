package wheel.reactive.impl;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.collections.iterators.EmptyIterator;
import org.apache.commons.collections.iterators.SingletonIterator;

import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;

public class RegisterImpl<VO> implements Register<VO> {


	class MyOutput extends AbstractSignal<VO> implements java.io.Serializable {

		@Override
		public VO currentValue() {
			return _currentValue;
		}

		private static final long serialVersionUID = 1L;

		@Override
		public int currentSize() {
			return _currentValue == null
				? 0
				: 1;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Iterator<VO> iterator() {
			if (_currentValue == null) return EmptyIterator.INSTANCE;
			return new SingletonIterator(_currentValue);
		}
	}


	class MySetter implements Omnivore<VO>, Serializable { private static final long serialVersionUID = 1L;

		@Override
		public void consume(VO newValue) {
			if (isSameValue(newValue)) throw new IllegalArgumentException("New value must be different.");
			_currentValue = newValue;
			
			_output.notifyReceivers(newValue);
		}
		
	}


	private VO _currentValue;
	private final Omnivore<VO> _setter = new MySetter();
	private final AbstractSignal<VO> _output = new MyOutput();
	
	
	public RegisterImpl(VO initialValue) {
		if (initialValue == null) return;
		setter().consume(initialValue);
	}


	public boolean isSameValue(VO value) {
		if (value == _currentValue) return true; 
		if (value != null && value.equals(_currentValue)) return true;
		return false;
	}


	public Signal<VO> output() {
		return _output;
	}


	public Omnivore<VO> setter() {
		return _setter;
	}

	private static final long serialVersionUID = 1L;
}
