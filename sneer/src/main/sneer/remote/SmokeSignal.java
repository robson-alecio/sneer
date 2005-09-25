package sneer.remote;

import java.io.Serializable;

public class SmokeSignal<T> implements Serializable {

	private final int _indianId;
	private final T _newValue;

	@SuppressWarnings("unchecked")
	SmokeSignal(int indianId, Object newValue) {
		_newValue = (T)newValue;
		_indianId = indianId;
	}
	
	int indianId() {
		return _indianId;
	}

	T newValue() {
		return _newValue;
	}

	private static final long serialVersionUID = 1L;

}
