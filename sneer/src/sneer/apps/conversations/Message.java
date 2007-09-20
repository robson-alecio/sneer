package sneer.apps.conversations;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	public Message(String text) {
		_text = text;
	}

	public final String _text;

}
