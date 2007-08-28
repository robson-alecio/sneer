package sneer.kernel.business.contacts;

import java.io.Serializable;

import wheel.graphics.JpgImage;

public class ContactInfo implements Serializable {

	public final String _nick;
	public final String _host;
	public final int _port;
	public final String _publicKey;
	
	public final String _thoughtOfTheDay;
	public final JpgImage _picture;
	public final String _profile;

	public ContactInfo(String nick, String host, int port, String publicKey, String thoughtOfTheDay, JpgImage picture, String profile) {
		_nick = nick;
		_host = host;
		_port = port;
		
		publicKey.toString();
		_publicKey = publicKey;
		
		_thoughtOfTheDay = thoughtOfTheDay;
		_picture = picture;
		_profile = profile;
	}

	private static final long serialVersionUID = 1L;

}
