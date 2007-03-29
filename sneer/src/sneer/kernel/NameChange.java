package sneer.kernel;

import java.util.Date;

import org.prevayler.Transaction;

import wheel.io.ui.User;

public class NameChange implements Transaction {

	private final String _name;

	public NameChange(User user, Essence essence) {
		_name = user.answer(" What is your name?" +
			"\n (You can change it any time you like)", essence.ownName());
	}
	
	public void executeOn(Object domain, Date ignored) {
		((Essence)domain).ownName(_name);
	}
	

	private static final long serialVersionUID = 1L;
}
