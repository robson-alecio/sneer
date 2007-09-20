package sneer.kernel.communication.impl;

import static wheel.i18n.Language.translate;

import java.io.IOException;
import java.io.NotSerializableException;

import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.communication.Connection;
import wheel.io.Log;
import wheel.io.network.ObjectSocket;
import wheel.io.network.OldNetwork;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;

public class ConnectionImpl implements Connection {

	private static final int BARK_PERIOD_MILLIS = 5000;
	private static final String BARK = "Bark";

	private final ContactAttributes _contact;
	private final OldNetwork _network;
	private final Consumer<OutgoingConnectionAttempt> _connectionValidator;
	private final Omnivore<Object> _objectReceiver;

	private final Object _socketOpenMonitor = new Object();
	private final SocketHolder _socketHolder = new SocketHolder(socketActivityReceiver());

	private volatile boolean _isClosed = false;

	private volatile long _lastActivityTime;
	
	private final PriorityQueue<ChannelPacket> _priorityQueue = new PriorityQueue<ChannelPacket>(10);
	
	private boolean _classNotFoundExceptionAlreadyLogged = false;
	private final Source<Boolean> _isOnlineSource = new SourceImpl<Boolean>(false);
	

	ConnectionImpl(ContactAttributes contact, OldNetwork network, Consumer<OutgoingConnectionAttempt> connectionValidator, Omnivore<Object> objectReceiver) {
		_contact = contact;
		_network = network;
		_connectionValidator = connectionValidator;
		_objectReceiver = objectReceiver;
		
		startIsOnlineWatchdog();
		startSender();
	}

	private Omnivore<Boolean> socketActivityReceiver() {
		return new Omnivore<Boolean>() { public void consume(Boolean socketActive) {
			if (socketActive)
				startReceiving(_socketHolder.socket());

			notifyIsOnline(socketActive);
		}};
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
			send(BARK);

			while (!_isClosed) {
				if ((System.currentTimeMillis() - _lastActivityTime) > BARK_PERIOD_MILLIS)
					send(BARK);
				
				if ((System.currentTimeMillis() - _lastActivityTime) > (BARK_PERIOD_MILLIS * 3))
					_socketHolder.crashSocket();

				Threads.sleepWithoutInterruptions(BARK_PERIOD_MILLIS);
			}
		}});
	}

	private ObjectSocket tryToProduceSocket() {
		if (_socketHolder.isEmpty()) competeToOpenSocket();
		return _socketHolder.socket();
	}


	private void competeToOpenSocket() {
		synchronized (_socketOpenMonitor) {
			if (!_socketHolder.isEmpty()) return; 
			ObjectSocket newSocket = tryToOpenSocket();
			if (newSocket == null) return;
			_socketHolder.setSocketIfNecessary(newSocket);
		}
	}

	private ObjectSocket tryToOpenSocket() {
		ObjectSocket result;
		try {
			result = _network.openSocket(host(), port());
		} catch (IOException e) {
			return null;
		}
		
		try {
			_connectionValidator.consume(new OutgoingConnectionAttempt(_contact, result));
		} catch (IllegalParameter e) {
			return null;
		}
		
		return result;
	}

	private Integer port() {
		return _contact.port().currentValue();
	}

	private String host() {
		return _contact.host().currentValue();
	}

	void serveAcceptedSocket(ObjectSocket incomingSocket) {
		_socketHolder.setSocketIfNecessary(incomingSocket);
	}

	private void startReceiving(final ObjectSocket mySocket) {
		Threads.startDaemon(new Runnable() { @Override public void run() {
			while (true) {
				try {
					tryToReceiveFrom(mySocket);
				} catch (IOException e) {
					_socketHolder.crash(mySocket);
					break;
				} catch (ClassNotFoundException e) {
					logOnlyFirstClassNotFoundExceptionToAvoidLogOverflow(e);
					break;
				} 
			}
		}});

	}

	private void tryToReceiveFrom(ObjectSocket socket) throws IOException, ClassNotFoundException {
		Object received = socket.readObject();

		_lastActivityTime = System.currentTimeMillis();
		if (BARK.equals(received)) return;
		//System.out.println("Received: " + received);

		ChannelPacket packet = (ChannelPacket)received;
		packet._packet._contactId = _contact.id();
		_objectReceiver.consume(packet);
	}

	private void logOnlyFirstClassNotFoundExceptionToAvoidLogOverflow(ClassNotFoundException e) {
		if (_classNotFoundExceptionAlreadyLogged) return;
		_classNotFoundExceptionAlreadyLogged = true;

		String nick = _contact.nick().currentValue();
		String message = translate("%1$s tried to send you objects of class you do not have installed. You might be running a different version of Sneer.", nick);
		Log.log(message + "\n" + e.getMessage());
	};

	void close() {
		_isClosed = true;
	}

	private void notifyIsOnline(boolean isOnline) {
		if (_isOnlineSource.output().currentValue() == isOnline) {
			Log.logStackWithMessage("isOnline state change redundant: " + isOnline);
			return;
		}
		
		_isOnlineSource.setter().consume(isOnline);
	}

	private void send(Object toSend) {  // Implement: Avoid losing important objects that could not be sent.
		ObjectSocket socket = tryToProduceSocket();
		if (socket == null) return;

		try {
			socket.writeObject(toSend);
		} catch (NotSerializableException nse) {
			_socketHolder.crash(socket);
			Log.log(nse);
		} catch (IOException e) {
			_socketHolder.crash(socket);
			return;
		}
		
		_lastActivityTime = System.currentTimeMillis();
	}

	void send(ChannelPacket channelPacket, int priority) {
		_priorityQueue.add(channelPacket, priority);
	}

	@Override
	public Signal<Boolean> isOnline() {
		return _isOnlineSource.output();
	}

}
