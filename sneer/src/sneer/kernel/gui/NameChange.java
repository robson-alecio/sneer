package sneer.kernel.gui;

import java.util.Date;

import org.prevayler.Transaction;

import sneer.kernel.business.essence.Essence;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;

public class NameChange implements Transaction {

	private final String _name;

	public NameChange(User user, String currentName) throws CancelledByUser {
		_name = user.answer(" What is your name?" +
			"\n (You can change it any time you like)", currentName);
	}
	
	public void executeOn(Object domain, Date ignored) {
		((Essence)domain).ownName(_name);
	}
	

	private static final long serialVersionUID = 1L;
}
