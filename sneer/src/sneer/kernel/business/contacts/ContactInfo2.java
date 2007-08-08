package sneer.kernel.business.contacts;

import java.io.Serializable;

public class ContactInfo2 implements Serializable {

	public final String _nick;
	public final String _host;
	public final int _port;
	public final String _publicKey;

	public ContactInfo2(String nick, String host, int port, String publicKey) {
		_nick = nick;
		_host = host;
		_port = port;

		publicKey.toString();
		_publicKey = publicKey;
	}

	private static final long serialVersionUID = 1L;

}
