package sneer.kernel.gui;

import java.util.Date;

import org.prevayler.Transaction;

import sneer.kernel.business.essence.Essence;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;

public class NewContactAddition implements Transaction {

	private final String _nick;
	private final String _host;
	private final int _port;

	public NewContactAddition(User user) throws CancelledByUser {
		_nick = user.answer("New contact's nickname");
		_host = user.answer("Host Address for " + _nick, _nick + ".dyndns.org");
		_port = port(user);
	}

	private int port(User user) throws CancelledByUser {
		return user.answerWithNumber("Sneer Port Number for " + _nick);
	}

	public void executeOn(Object essence, Date ignored) {
		((Essence)essence).addContact(_nick, _host, _port);
	}

	private static final long serialVersionUID = 1L;
}
