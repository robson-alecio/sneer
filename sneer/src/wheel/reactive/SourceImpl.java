package wheel.reactive;

import java.io.Serializable;

public class SourceImpl<T> extends AbstractSignal<T> implements Source<T>, Serializable  {

	private T _currentValue;
	
	public void supply(T newValue) {
		if (isSameValue(newValue)) throw new IllegalArgumentException("New value must be different.");
		_currentValue = newValue;
		notifyReceivers(newValue);
	}

	public boolean isSameValue(T value) {
		if (value == _currentValue) return true; 
		if (value != null && value.equals(_currentValue)) return true;
		return false;
	}

	public T currentValue() {
		return _currentValue;
	}

	private static final long serialVersionUID = 1L;


}
