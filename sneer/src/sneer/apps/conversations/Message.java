package sneer.apps.conversations;

public class Message {

	public Message(String text, String destination) {
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
