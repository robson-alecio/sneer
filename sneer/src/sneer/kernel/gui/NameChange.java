package sneer.kernel.gui;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.io.ui.TrayIcon.Action;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public class NameChange extends CancellableAction {
	
	NameChange(User user, Signal<String> ownName, Omnivore<String> ownNameSetter) {
		_user = user;
		_ownName = ownName;
		_ownNameSetter = ownNameSetter;
	}
	
	private final Signal<String> _ownName;
	private final User _user;
	private final Omnivore<String> _ownNameSetter;

	public String caption() {
		return "Change your Name";
	}

	@Override
	public void tryToRun() throws CancelledByUser {
		String currentName = _ownName.currentValue();
		String newName = _user.answer(" What is your name?" +
				"\n (You can change it any time you like)", currentName);
		_ownNameSetter.consume(newName);
	}

}
