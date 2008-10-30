package sneer.pulp.connection.impl;

import static wheel.io.Logger.logShort;

import java.io.IOException;
import java.util.Arrays;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.threadpool.ThreadPool;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class ByteConnectionImpl implements ByteConnection {

	@Inject
	static private KeyManager _keyManager;
	
	@Inject
	static private ThreadPool _threadPool;
	
	private final Register<Boolean> _isOnline = new RegisterImpl<Boolean>(false);

	private final SocketHolder _socketHolder = new SocketHolder(_isOnline.setter());
	
	private volatile PacketScheduler _scheduler;

	private final String _label;

	private final Contact _contact;
	
	private volatile Omnivore<byte[]> _receiver;

	ByteConnectionImpl(String label, Contact contact) {
		_label = label;
		_contact = contact;
		startSending();
		startReceiving();
	}

	@Override
	public String toString() {
		return _label + " - " + _contact.nickname();
	}

	
	@Override
	public Signal<Boolean> isOnline() {
		return _isOnline.output();
	}
	

	void manageOutgoingSocket(ByteArraySocket newSocket) {
		if (!tryToManageOutgoingSocket(newSocket))
			newSocket.crash();
	}

	private boolean tryToManageOutgoingSocket(ByteArraySocket newSocket) {
		if (!_socketHolder.isEmpty()) return false;
		if (!shakeHands(newSocket)) return false;
		
		_socketHolder.setSocketIfNecessary(newSocket);
		return true;
	}

	private boolean shakeHands(ByteArraySocket socket) {
		try {
			return tryToShakeHands(socket);
		} catch (IOException e) {
			logShort(e, "Exception while shaking hands in outgoing connection.");
			return false;
		}
	}

	private boolean tryToShakeHands(ByteArraySocket socket) throws IOException {
		socket.write(ProtocolTokens.SNEER_WIRE_PROTOCOL_1);
		socket.write(_keyManager.ownPublicKey().bytes());
		byte[] response = socket.read();
		return Arrays.equals(ProtocolTokens.OK, response);
	}

	void manageIncomingSocket(ByteArraySocket socket) {
		_socketHolder.setSocketIfNecessary(socket);
	}

	private boolean tryToSend(byte[] array) {

		ByteArraySocket mySocket = _socketHolder.socket();
		if (mySocket == null) return false;
		
		try {
			mySocket.write(array);
			return true;
		} catch (IOException iox) {
			crash(mySocket, iox, "Error trying to send packet. ");
			return false;
		}
	}


	public void setReceiver(Omnivore<byte[]> receiver) {
		if (_receiver != null) throw new IllegalStateException();
		_receiver = receiver;
	}
	
	private void startSending() {
		_threadPool.registerActor(new Runnable() { @Override public void run() {
			while (true) {
				byte[] packet = waitForNextPacket();
				if (tryToSend(packet))
					_scheduler.previousPacketWasSent();
				else
					Threads.sleepWithoutInterruptions(10); //Optimize Use wait/notify.
			}
		}});
	}
	
	private byte[] waitForNextPacket() {
		while (true) {
			if (_scheduler == null) { //Fix: When the old protocol dies, the _sender should never be null. It no longer needs to be volatile either. 
				Threads.sleepWithoutInterruptions(10);
				continue;
			}
			return _scheduler.highestPriorityPacketToSend();
		}
	}	

	private void startReceiving() {
		_threadPool.registerActor(new Runnable() { @Override public void run() {
			while (true) {
				if (!tryToReceive())
					Threads.sleepWithoutInterruptions(10); //Optimize Use wait/notify
			}
		}});
	}

	private boolean tryToReceive() {
		ByteArraySocket mySocket = _socketHolder.socket();
		if (mySocket ==  null) return false;

		try {
			_receiver.consume(mySocket.read());
			return true;
		} catch (Exception e) {
			crash(mySocket, e, "Error trying to receive packet.");
			return false;
		} 
	}

	private void crash(ByteArraySocket mySocket, Exception e,
			final String message) {
		logShort(e, message);
		_socketHolder.crash(mySocket);
	}

	@Override
	public void setSender(PacketScheduler sender) {
		if (_scheduler != null) throw new IllegalStateException();
		_scheduler = sender;
	}

}