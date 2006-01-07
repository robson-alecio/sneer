package sneer.old.remote;

import java.io.Serializable;

public abstract class AbstractSmokeSignal extends SmokeSignal implements Serializable {

	protected AbstractSmokeSignal(int indianId) {
		super(indianId);
	}

}
