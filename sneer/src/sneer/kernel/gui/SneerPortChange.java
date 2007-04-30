package sneer.kernel.gui;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.Consumer;
import wheel.lang.IntegerParser;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public class SneerPortChange extends CancellableAction {
	
	SneerPortChange(User user, Signal<?> signal, Consumer<Integer> setter) {
		_user = user;
		_signal = signal;
		_setter = new IntegerParser(setter); //Fix: limit to 65535.
	}
	
	private final User _user;
	private final Signal<?> _signal;
	private final Consumer<String> _setter;

	public String caption() {
		return "Sneer Port Configuration";
	}

	@Override
	public void tryToRun() throws CancelledByUser {
		String errorMessage = "";
		String current = _signal.currentValue().toString();
		
		while (true) {
			String newValue = _user.answer(errorMessage + " Change this only if you know what you are doing." +
					"\n Sneer IP port to listen:", current);
			try {
				_setter.consume(newValue);
				return;
			} catch (IllegalParameter e) {
				errorMessage = e.getMessage() + "\n Try again.\n\n";
				current = newValue;
			}
		}
	}

}
