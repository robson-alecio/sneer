package wheelexperiments.reactive;

import java.io.Serializable;

public class SourceImpl<T> extends AbstractSignal<T> implements Source<T>, Serializable  {

	private T _currentValue;
	
	public void supply(T newValue) {
		assertValueChanging(newValue);
		_currentValue = newValue;
		notifyReceivers(newValue);
	}

	private void assertValueChanging(T newValue) {
		if (newValue == _currentValue) throw new IllegalArgumentException("New value must be different.");
		if (newValue != null && newValue.equals(_currentValue))  throw new IllegalArgumentException("New value must be different.");
	}

	public T currentValue() {
		return _currentValue;
	}

	private static final long serialVersionUID = 1L;


}
