package sneer.kernel.communication.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.OnlineEvent;
import wheel.io.Log;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.lang.exceptions.IllegalParameter;

public class ConnectionImpl {

	private static final int BARK_PERIOD_MILLIS = 5000;
	private static final String BARK = "Bark";

	private static class InvalidConnectionAttempt extends Exception { private static final long serialVersionUID = 1L; }

	private final Contact _contact;
	private final OldNetwork _network;
	private volatile ObjectSocket _socket;
	private final Omnivore<OnlineEvent> _onlineSetter;
	private final Consumer<OutgoingConnectionAttempt> _connectionValidator;
	private final Omnivore<Object> _objectReceiver;

	private volatile boolean _isClosed = false;
	private volatile long _lastActivityTime;
	
	private final Object _socketOpenMonitor = new Object();
	private final Object _socketSetterMonitor = new Object();
	
	private final PriorityQueue<ChannelPacket> _priorityQueue = new PriorityQueue<ChannelPacket>(10);
	

	public ConnectionImpl(Contact contact, OldNetwork network, Omnivore<OnlineEvent> onlineSetter, Consumer<OutgoingConnectionAttempt> connectionValidator, Omnivore<Object> objectReceiver) { //Refactor: move online notification to the objectReceiver instead of having separate onlineSetter.
		_contact = contact;
		_network = network;
		_onlineSetter = onlineSetter;
		_connectionValidator = connectionValidator;
		_objectReceiver = objectReceiver;
		
		startIsOnlineWatchdog();
		startSender();
	}

	private void startSender() {
		Threads.startDaemon(new Runnable() { public void run() {
			while (true) {
				send(_priorityQueue.waitForNext());
			}
		}});
	}

	private void startIsOnlineWatchdog() {
		Threads.startDaemon(new Runnable(){	@Override public void run() {
			while (!_isClosed) {
				if ((System.currentTimeMillis() - _lastActivityTime) > BARK_PERIOD_MILLIS)
					send(BARK);
				
				if ((System.currentTimeMillis() - _lastActivityTime) > (BARK_PERIOD_MILLIS * 2))
					setIsOnline(false);

				if ((System.currentTimeMillis() - _lastActivityTime) > (BARK_PERIOD_MILLIS * 4))
					closeSocket();
				
				Threads.sleepWithoutInterruptions(BARK_PERIOD_MILLIS);
			}
		} } );
	}

	private ObjectSocket produceSocket() throws IOException, InvalidConnectionAttempt {
		while (_socket == null) competeToOpenSocket();
		return _socket;
	}

	private void competeToOpenSocket() throws IOException, InvalidConnectionAttempt {
		synchronized (_socketOpenMonitor) {
			if (_socket != null) return; 
			setSocketIfNecessary(openSocket());
		}
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

	private void setSocketIfNecessary(ObjectSocket socket) {
		synchronized (_socketSetterMonitor) {
			if (_socket != null) return;
			_socket = socket;
		}
		startReceiving();
	}

	void startReceiving() {
		Threads.startDaemon(new Runnable() { @Override public void run() {
			ObjectSocket mySocket = _socket;
			if (mySocket == null) return;

			while (mySocket == _socket) {
				try {
					Object received = mySocket.readObject();
					
					_lastActivityTime = System.currentTimeMillis();
					setIsOnline(true);
					
					if (BARK.equals(received)) continue;
					System.out.println("Received: " + received);

					ChannelPacket packet = (ChannelPacket)received;
					packet._packet._contactId = _contact.id();
					_objectReceiver.consume(packet);
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
		_isClosed = true;
		closeSocket();
	}

	private void closeSocket() {
		if (_socket == null) return;
		try {
			_socket.close();
		} catch (IOException e) {
		} finally {
			_socket = null;
		}
	}

	private void setIsOnline(boolean isOnline) {
		Boolean wasOnline = _contact.isOnline().currentValue();
		if (wasOnline != isOnline) 	_onlineSetter.consume(new OnlineEvent(_contact.nick().currentValue(), isOnline));
	}

	private void send(Object toSend) { // Fix: keep in a queue not to lose objects.
		try {
			produceSocket().writeObject(toSend);
			_lastActivityTime = System.currentTimeMillis();
		} catch (IOException e) {
			_socket = null;
		} catch (InvalidConnectionAttempt e) {
			_socket = null;
		}
	}

	void send(ChannelPacket channelPacket, int priority) {
		_priorityQueue.add(channelPacket, priority);
	}

}
