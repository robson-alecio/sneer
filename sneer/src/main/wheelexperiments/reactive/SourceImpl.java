package wheelexperiments.reactive;

import java.io.Serializable;

public class SourceImpl<T> extends Notifier<T> implements Source<T>, Serializable  {

	private T _currentValue;
	
	public SourceImpl() {}

	public void supply(T newValue) {
		if (newValue == _currentValue) return;
		_currentValue = newValue;
		notifyReceivers();
	}

	public T currentValue() {
		return _currentValue;
	}

	private static final long serialVersionUID = 1L;

}
