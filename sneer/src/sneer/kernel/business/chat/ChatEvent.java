package sneer.kernel.business.chat;

import org.jmock.util.NotImplementedException;

public class ChatEvent {

	private final String _text;

	public ChatEvent(String text) {
		_text = text;
	}

	public String text() {
		return _text;
	}
	
	@Override
	public String toString() {
		return ChatEvent.class.getName() + " -> " + text();
	}

}
