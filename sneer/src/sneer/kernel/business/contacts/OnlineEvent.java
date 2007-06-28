package sneer.kernel.business.contacts;

import java.io.Serializable;

public class OnlineEvent implements Serializable {

	public final String _nick;
	public final boolean _isOnline;

	public OnlineEvent(String nick, boolean isOnline) { //Fix Use ContactId instead of nick.
		_nick = nick;
		_isOnline = isOnline;
	}

	private static final long serialVersionUID = 1L;
}
