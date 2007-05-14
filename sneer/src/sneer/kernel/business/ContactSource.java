package sneer.kernel.business;

import java.io.Serializable;

import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.SourceImpl;

public class ContactSource implements Contact, Serializable { private static final long serialVersionUID = 1L;

	public ContactSource(String nick, String host, int port) {
		_nick = new SourceImpl<String>(nick);
		_host = new SourceImpl<String>(host);
		_port = new SourceImpl<Integer>(port);
	}

	
	private final Source<String> _nick;
	private final Source<String> _host;
	private final Source<Integer> _port;
	private transient Source<Boolean> _isOnline;  //Fix: consider removing transient and using transient listeners.


	public Signal<Boolean> isOnline() {
		return lazyIsOnline().output();
	}


	private synchronized Source<Boolean> lazyIsOnline() {
		if (_isOnline == null) _isOnline = new SourceImpl<Boolean>(Boolean.FALSE);
		
		return _isOnline;
	}


	@Override
	public Signal<String> host() {
		return _host.output();
	}


	@Override
	public Signal<String> nick() {
		return _nick.output();
	}


	@Override
	public Signal<Integer> port() {
		return _port.output();
	}

}
