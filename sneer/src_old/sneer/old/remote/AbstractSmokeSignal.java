package sneer.old.remote;

import java.io.Serializable;

public abstract class AbstractSmokeSignal extends SmokeSignal implements Serializable {

	private static final long serialVersionUID = 1L;

	protected AbstractSmokeSignal(int indianId) {
		super(indianId);
	}

}
