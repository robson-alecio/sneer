package sneer.remote;

import java.io.Serializable;

public class SmokeSignal implements Serializable {

	private final int _indianId;
	private final String _newValue;

	SmokeSignal(int indianId, String newValue) {
		_newValue = newValue;
		_indianId = indianId;
	}
	
	int indianId() {
		return _indianId;
	}

	String newValue() {
		return _newValue;
	}

	private static final long serialVersionUID = 1L;

}
