package sneer;

import java.util.Date;

import org.prevayler.Transaction;

import sneer.Sneer.User;

public class ContactAddition implements Transaction {

	private final String _nickname;
	private final String _tcpAddress;
	
	public ContactAddition(User _user) {
		_nickname = _user.giveNickname();
		_tcpAddress = _user.informTcpAddress("localhost:" + Home.DEFAULT_PORT);
	}

	public void executeOn(Object home, Date ignored) {
		((Home)home).addContact(_nickname, _tcpAddress);
	}

	private static final long serialVersionUID = 1L;
}
