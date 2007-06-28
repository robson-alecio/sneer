package sneer.kernel.business.contacts;

import java.io.Serializable;

public class ContactInfo implements Serializable {

	public final String _nick;
	public final String _host;
	public final int _port;

	public ContactInfo(String nick, String host, int port) {
		_nick = nick;
		_host = host;
		_port = port;
	}

	private static final long serialVersionUID = 1L;

}
