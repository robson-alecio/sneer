package sneer;

import java.util.Date;

import org.prevayler.Transaction;

import sneer.Sneer.User;

public class ContactAddition implements Transaction {

	private final String _nickname;
	private final String _ipAddress;
	private final int _port;

	private boolean _cancelled;
	
	public ContactAddition(User user) {
		_nickname = user.giveNickname();
		
		String tcpAddress = user.informTcpAddress("localhost:" + Home.DEFAULT_PORT);
		String[] addressParts = tcpAddress.split(":");
		_ipAddress = addressParts[0];
		int port = Home.DEFAULT_PORT;
		if (addressParts.length > 1) {
			port = Integer.parseInt(addressParts[1]);
		}
		
		if (port < 1 || port > 65535) {
			user.acknowledge("Error: Port must be between 1 and 65535.");
			cancelWizard();
		}
		
		_port = port;
	}

	private void cancelWizard() {
		_cancelled = true;
	}

	public boolean cancelled() {
		return _cancelled;
	}

	public void executeOn(Object home, Date ignored) {
		((Home)home).addContact(_nickname, _ipAddress, _port);
	}

	private static final long serialVersionUID = 1L;

}
