package sneer.apps.messages;

import org.jmock.util.NotImplementedException;

public class ChatEvent {

	public ChatEvent(String text, String destination) {
		_text = text;
		_destination = destination;
	}

	public final String _text;
	public final String _destination;
	
	@Override
	public String toString() {
		return "To " + _destination + ": " + _text;
	}

}
