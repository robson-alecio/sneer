package sneer.apps.asker;

import wheel.lang.Omnivore;

public class AskerRequest {

	public final String _message;
	public final Omnivore<Boolean> _callback;

	public AskerRequest(String message, Omnivore<Boolean> callback) {
		_message = message;
		_callback = callback;
	}

}
