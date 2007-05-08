package sneer.kernel.business;

import java.io.Serializable;

public class ContactInfo implements Serializable {

	final String _nick;
	final String _host;
	final int _port;

	public ContactInfo(String nick, String host, int port) {
		_nick = nick;
		_host = host;
		_port = port;
	}

	private static final long serialVersionUID = 1L;

}
