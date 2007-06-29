package sneer.kernel.communication.impl;

import java.io.IOException;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.OnlineEvent;
import wheel.io.Connection;
import wheel.io.Log;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;

public class ConnectionImpl implements Connection {

	private final Contact _contact;
	private final OldNetwork _network;
	private ObjectSocket _socket;
	private final String _ownPublicKey;
	private final Signal<String> _ownName;
	private final Omnivore<OnlineEvent> _onlineSetter;

	public ConnectionImpl(Contact contact, OldNetwork network, String publicKey, Signal<String> ownName, Omnivore<OnlineEvent> onlineSetter) {
		_contact = contact;
		_network = network;
		_ownPublicKey = publicKey;
		_ownName = ownName;
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
		if (_socket == null) setSocketIfNecessary(openSocket());
		return _socket;
	}

	private ObjectSocket openSocket() throws IOException {
		String host = _contact.host().currentValue();
		int port = _contact.port().currentValue();

		ObjectSocket result = _network.openSocket(host, port);
		result.writeObject(_ownPublicKey);
		result.writeObject(_ownName.currentValue());
		return result;
	}

	void serveIncomingSocket(ObjectSocket socket) {
		setSocketIfNecessary(socket);
	}

	synchronized private void setSocketIfNecessary(ObjectSocket socket) {
		if (_socket != null) return;
		_socket = socket;
		startReceiving();
	}

	void startReceiving() {
		Threads.startDaemon(new Runnable() { @Override public void run() {
			while (true){
				try {
					Object readObject = _socket.readObject();
					System.out.println("Received: " + readObject);
				} catch (Exception e) {
					break;
					// Implement This is the moment where a disconnection occurs. Inform the online watchdog to set the contact offline.
				} 
			}
		}});
	}

}
