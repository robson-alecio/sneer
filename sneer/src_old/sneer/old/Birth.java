package sneer.old;

import java.util.Date;

import org.prevayler.Transaction;

import sneer.old.Sneer.User;

public class Birth implements Transaction {

	private final String _name;
	private final int _serverPort;

	Birth(User user) {
		_name = user.confirmName("Your Name");
		_serverPort = user.confirmServerPort(Home.DEFAULT_PORT);
	}
	
	public void executeOn(Object home, Date ignored) {
		((Home)home).foster(_name, _serverPort);
	}

	private static final long serialVersionUID = 1L;

}
