package sneer.kernel.business.essence;

import java.io.Serializable;

public class ContactSource implements Contact, Serializable { private static final long serialVersionUID = 1L;

	public ContactSource(String nick, String host, int port) {
		_nick = nick;
		_host = host;
		_port = port;
	}

	
	private final String _nick;
	private final String _host;
	private final int _port;


	@Override
	public String toString() {
		return _nick + " - " + _host + ":" + _port;
	}

}
