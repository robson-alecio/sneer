package sneer.bricks.connection.impl;

import java.io.IOException;
import java.util.Arrays;

import sneer.bricks.connection.Connection;
import sneer.bricks.contacts.Contact;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.log.Logger;
import sneer.bricks.network.ByteArraySocket;
import sneer.bricks.threadpool.ThreadPool;
import sneer.lego.Inject;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class ConnectionImpl implements Connection {

	@Inject
	static private KeyManager _keyManager;
	
	@Inject
	static private Logger _logger;

	@Inject
	static private ThreadPool _threadPool;
	
	private Register<Boolean> _isOnline = new RegisterImpl<Boolean>(false);

	private final SocketHolder _socketHolder = new SocketHolder(_isOnline.setter());

	private volatile Omnivore<byte[]> _receiver;

	private final String _label;

	private final Contact _contact;
	
	ConnectionImpl(String label, Contact contact) {
		_label = label;
		_contact = contact;
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
			_logger.info(e.getMessage());
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
		//Implement: Reject if my own pk.
		//Challenge pk.
		
		_socketHolder.setSocketIfNecessary(socket);
		
	}
	
	@Override
	public void send(byte[] array) {
		while (!tryToSend(array))
			Threads.sleepWithoutInterruptions(10); //Optimize Use wait/notify.
	}


	private boolean tryToSend(byte[] array) {

		ByteArraySocket mySocket = _socketHolder.socket();
		if (mySocket == null) return false;
		
		try {
			mySocket.write(array);
			return true;
		} catch (IOException iox) {
			_logger.info(iox.getMessage(), iox);
			_socketHolder.crash(mySocket);
			return false;
		}
	}


	public void setReceiver(Omnivore<byte[]> receiver) {
		if (_receiver != null) throw new IllegalStateException();
		_receiver = receiver;
	}
	
	private void startReceiving() {
		_threadPool.registerActor(new Runnable() { @Override public void run() {
			while (true) {
				byte[] packet = tryToReceive();
				if (packet == null)
					Threads.sleepWithoutInterruptions(10); //Optimize Use wait/notify
				else
					_receiver.consume(packet);
			}
		}});
	}


	private byte[] tryToReceive() {
		ByteArraySocket mySocket = _socketHolder.socket();
		if (mySocket ==  null) return null;
		
		try {
			return mySocket.read();
		} catch (IOException e) {
			_logger.info(e.getMessage(), e);
			_socketHolder.crash(mySocket);
			throw new RuntimeException(e);
			//return null;
		} 

	}

}
