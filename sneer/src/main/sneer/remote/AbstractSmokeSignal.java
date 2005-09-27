package sneer.remote;

import java.io.Serializable;

public abstract class AbstractSmokeSignal implements SmokeSignal, Serializable {

	protected final int _indianId;

	protected AbstractSmokeSignal(int indianId) {
		_indianId = indianId;
	}
	public int indianId() {
		return _indianId;
	}

}
