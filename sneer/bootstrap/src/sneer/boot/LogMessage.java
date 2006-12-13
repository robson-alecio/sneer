package sneer.boot;

import sneer.server.Command;
import wheelexperiments.Log;

public class LogMessage implements Command {

	private final String _message;

	public LogMessage(String message) {
		_message = message;
	}

	public void execute() {
		Log.log(_message);
		return;
	}

	private static final long serialVersionUID = 1L;
}
