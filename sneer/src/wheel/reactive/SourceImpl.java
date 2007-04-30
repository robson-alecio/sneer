package wheel.reactive;

import java.io.Serializable;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.exceptions.IllegalParameter;

public class SourceImpl<VO> extends AbstractSignal<VO> implements Source<VO>, Signal<VO>, Omnivore<VO>, Serializable  {

	
	private VO _currentValue;
	
	
	public SourceImpl(VO initialValue) {
		setter().consume(initialValue);
	}


	public void consume(VO newValue) {
		if (isSameValue(newValue)) throw new IllegalArgumentException("New value must be different.");
		_currentValue = newValue;
		notifyReceivers(newValue);
	}

	
	public boolean isSameValue(VO value) {
		if (value == _currentValue) return true; 
		if (value != null && value.equals(_currentValue)) return true;
		return false;
	}


	public VO currentValue() {
		return _currentValue;
	}


	public Signal<VO> output() {
		return this;
	}


	public Omnivore<VO> setter() {
		return this;
	}
	
	
	private static final long serialVersionUID = 1L;

}
