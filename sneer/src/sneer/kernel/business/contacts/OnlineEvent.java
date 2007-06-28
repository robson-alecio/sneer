package sneer.kernel.business.contacts;

public class OnlineEvent {

	public final String _nick;
	public final boolean _isOnline;

	public OnlineEvent(String nick, boolean isOnline) { //Fix Use ContactId instead of nick.
		_nick = nick;
		_isOnline = isOnline;
	}

}
