package sneer.kernel.communication.impl;

import java.io.IOException;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.OnlineEvent;
import wheel.io.Connection;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;

public class ConnectionImpl implements Connection {

	private final Contact _contact;
	private final OldNetwork _network;
	private ObjectSocket _socket;
	private final String _publicKey;
	private final Omnivore<OnlineEvent> _onlineSetter;

	public ConnectionImpl(Contact contact, OldNetwork network, String publicKey, Omnivore<OnlineEvent> onlineSetter) {
		_contact = contact;
		_network = network;
		_publicKey = publicKey;
		_onlineSetter = onlineSetter;
		
		startIsOnlineWatchdog();
	}

	public Signal<Object> input() {
		return null;
	}

	public Omnivore<Object> output() {
		return null;
	}


	private void startIsOnlineWatchdog() {
		Threads.startDaemon(new Runnable(){	@Override public void run() {
			while (true) {
				bark();
				Threads.sleepWithoutInterruptions(15000);
			}
		} } );
	}

	private void bark() {
		boolean isOnline = isOnline();

		Boolean wasOnline = _contact.isOnline().currentValue();
		if (isOnline == wasOnline) return;
		
		String nick = _contact.nick().currentValue();
		_onlineSetter.consume(new OnlineEvent(nick, isOnline));
	}

	private boolean isOnline() {
		
		try {
			produceSocket().writeObject("Bark");
			return true;
		} catch (IOException e) {
			_socket = null;
			return false;
		}
	}

	private ObjectSocket produceSocket() throws IOException {
		if (_socket == null) _socket = openSocket();
		return _socket;
	}

	private ObjectSocket openSocket() throws IOException {
		String host = _contact.host().currentValue();
		int port = _contact.port().currentValue();

		ObjectSocket result = _network.openSocket(host, port);
		result.writeObject(_publicKey);
		return result;
	}
	
}
