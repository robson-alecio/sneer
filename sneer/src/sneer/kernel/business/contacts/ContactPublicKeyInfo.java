package sneer.kernel.business.contacts;

import java.io.Serializable;

public class ContactPublicKeyInfo implements Serializable {

	public final String _nick;
	public final String _publicKey;

	public ContactPublicKeyInfo(String nick, String publicKey) {
		_nick = nick;
		_publicKey = publicKey;
	}

	private static final long serialVersionUID = 1L;

}
