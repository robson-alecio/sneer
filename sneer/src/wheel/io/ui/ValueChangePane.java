package wheel.io.ui;

import sneer.kernel.gui.CancellableAction;
import wheel.lang.Consumer;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public class ValueChangePane extends CancellableAction {

	public ValueChangePane(String caption, String prompt, User user, Signal<?> signal, Consumer<String> setter) {
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
			if (newValue.equals(_signal.currentValue().toString())) return;
			try {
				_setter.consume(newValue);
				return;
			} catch (IllegalParameter e) {
				errorMessage = e.getMessage() + "\nTry again.\n\n";
				current = newValue;
			}
		}

	}

}
