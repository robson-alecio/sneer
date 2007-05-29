package sneer.kernel.business.contacts;

public class OnlineEvent {

	public final String _nick;
	public final boolean _isOnline;

	public OnlineEvent(String nick, boolean isOnline) {
		_nick = nick;
		_isOnline = isOnline;
	}

}
