package wheelexperiments.reactive;

import java.io.Serializable;

public class SourceImpl<T> extends AbstractSignal<T> implements Source<T>, Serializable  {

	private T _currentValue;
	
	public void supply(T newValue) {
		if (newValue == _currentValue) return;
		if (newValue != null && newValue.equals(_currentValue)) return;
		
		_currentValue = newValue;
		notifyReceivers(newValue);
	}

	public T currentValue() {
		return _currentValue;
	}

	private static final long serialVersionUID = 1L;


}
