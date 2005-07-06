package sneer.remote;

import java.io.Serializable;

class Envelope implements Serializable {

	private Object _contents;
	private final int _stamp;

	Envelope(Object contents, int stamp) {
		_contents = contents;
		_stamp = stamp;
	}
	
	Object contents() {
		return _contents;
	}

	void contents(Object contents) {
		_contents = contents;
	}
	
	int stamp() {
		return _stamp;
	}

	private static final long serialVersionUID = 1L;

}
