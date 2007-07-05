package sneer.kernel.communication.impl;

import java.io.IOException;
import java.net.UnknownHostException;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.OnlineEvent;
import wheel.io.Connection;
import wheel.io.Log;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;

public class ConnectionImpl implements Connection {

	private static final int BARK_PERIOD_MILLIS = 5000;
	private static final String BARK = "Bark";

	private static class InvalidConnectionAttempt extends Exception { private static final long serialVersionUID = 1L; }

	private final Contact _contact;
	private final OldNetwork _network;
	private volatile ObjectSocket _socket;
	private final Omnivore<OnlineEvent> _onlineSetter;
	private final Consumer<OutgoingConnectionAttempt> _connectionValidator;
	private volatile boolean _isClosed = false;
	private volatile long _lastReceivedObjectTime;
	private volatile long _lastSentObjectTime;

	public ConnectionImpl(Contact contact, OldNetwork network, Omnivore<OnlineEvent> onlineSetter, Consumer<OutgoingConnectionAttempt> connectionValidator) { //Refactor: Use the same Omnivore<Connection> to do online notification instead of having separate onlineSetter.
		_contact = contact;
		_network = network;
		_onlineSetter = onlineSetter;
		_connectionValidator = connectionValidator;
		
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
			while (!_isClosed) {
				if ((System.currentTimeMillis() - _lastSentObjectTime) > BARK_PERIOD_MILLIS)
					bark();
				
				if ((System.currentTimeMillis() - _lastReceivedObjectTime) > (BARK_PERIOD_MILLIS * 2))
					setIsOnline(false);
				
				Threads.sleepWithoutInterruptions(BARK_PERIOD_MILLIS);
			}
		} } );
	}

	private void bark() {
		try {
			produceSocket().writeObject(BARK);
			_lastSentObjectTime = System.currentTimeMillis(); //Fix: This is generic for any object sent not only bark.
		} catch (IOException e) {
			_socket = null;
		} catch (InvalidConnectionAttempt e) {
			_socket = null;
		}
	}

	private ObjectSocket produceSocket() throws IOException, InvalidConnectionAttempt {
		if (_socket == null) setSocketIfNecessary(openSocket());
		return _socket;
	}

	private ObjectSocket openSocket() throws IOException, InvalidConnectionAttempt {
		String host = _contact.host().currentValue();
		int port = _contact.port().currentValue();

		ObjectSocket result = _network.openSocket(host, port);
		try {
			_connectionValidator.consume(new OutgoingConnectionAttempt(_contact, result));
		} catch (IllegalParameter e) {
			throw new InvalidConnectionAttempt();
		}
		
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
			ObjectSocket mySocket = _socket;
			while (mySocket == _socket) {
				try {
					Object readObject = mySocket.readObject();
					System.out.println("Received: " + readObject);
					
					_lastReceivedObjectTime = System.currentTimeMillis();
					setIsOnline(true);

				} catch (IOException e) {
					break;
					// Implement This is the moment where a disconnection occurs. Inform the online watchdog to set the contact offline.
				} catch (ClassNotFoundException e) {
					Log.log(e);
					break;
				} 
			}
		}});
	}

	void close() {
		try {
			_isClosed = true;
			if (_socket != null) _socket.close();
			_socket = null;
		} catch (IOException ignored) {
		}
	}

	private void setIsOnline(boolean isOnline) {
		Boolean wasOnline = _contact.isOnline().currentValue();
		if (wasOnline != isOnline) 	_onlineSetter.consume(new OnlineEvent(_contact.nick().currentValue(), isOnline));
	}

}
