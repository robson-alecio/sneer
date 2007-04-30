package sneer.kernel.business;

import java.io.Serializable;

import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.SourceImpl;

public class ContactSource implements Contact, Serializable { private static final long serialVersionUID = 1L;

	public ContactSource(String nick, String host, int port) {
		_nick = nick;
		_host = host;
		_port = port;
	}

	
	private final String _nick;
	private final String _host;
	private final int _port;
	private transient Source<Boolean> _isOnline;


	@Override
	public String toString() {
		return onlineTag() + " " + _nick + " - " + _host + ":" + _port;
	}


	private String onlineTag() {
		return isOnline().currentValue().booleanValue()
			? "On  :) "
			: "Off :( ";
	}


	public Signal<Boolean> isOnline() {
		return lazyIsOnline().output();
	}


	private Source<Boolean> lazyIsOnline() {
		if (_isOnline == null) _isOnline = new SourceImpl<Boolean>(Boolean.FALSE);
		
		return _isOnline;
	}

}
