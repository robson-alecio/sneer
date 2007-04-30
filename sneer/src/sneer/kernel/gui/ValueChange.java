package sneer.kernel.gui;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.Consumer;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public class ValueChange extends CancellableAction {
	
	ValueChange(String caption, String prompt, User user, Signal<?> signal, Consumer<String> setter) {
		_caption = caption;
		_prompt = prompt;
		_user = user;
		_signal = signal;
		_setter = setter;
	}
	
	private final Signal<?> _signal;
	private final User _user;
	private final Consumer<String> _setter;
	private final String _caption;
	private final String _prompt;

	public String caption() {
		return _caption;
	}

	@Override
	public void tryToRun() throws CancelledByUser {
		String errorMessage = "";
		String current = _signal.currentValue().toString();
		
		while (true) {
			String newValue = _user.answer(errorMessage + _prompt, current);
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
