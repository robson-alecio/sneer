package sneer.pulp.connection.impl;

import static wheel.io.Logger.log;

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

	private static final byte LEGACY_PROTOCOL = 0;
	private static final byte NEW_PROTOCOL = 1;

	@Inject
	static private KeyManager _keyManager;
	
	@Inject
	static private ThreadPool _threadPool;
	
	private final Register<Boolean> _isOnline = new RegisterImpl<Boolean>(false);

	private final SocketHolder _socketHolder = new SocketHolder(_isOnline.setter());

	private final String _label;

	private final Contact _contact;
	
	private volatile Omnivore<byte[]> _legacyReceiver;
	private volatile Omnivore<byte[]> _receiver;
	
	ByteConnectionImpl(String label, Contact contact) {
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
			log(e);
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
	
	@Override
	public void legacySend(byte[] array) {
		byte[] packet = flagWithProtocol(array, LEGACY_PROTOCOL);
		while (!tryToSend(packet))
			Threads.sleepWithoutInterruptions(10); //Optimize Use wait/notify.
	}

	private byte[] flagWithProtocol(byte[] array, byte protocol) {
		byte[] result = makeSpaceForFlag(array);
		result[0] = protocol;
		return result;
	}

	private byte[] makeSpaceForFlag(byte[] array) {
		byte[] result = new byte[array.length + 1];
		System.arraycopy(array, 0, result, 1, array.length);
		return result;
	}


	private boolean tryToSend(byte[] array) {

		ByteArraySocket mySocket = _socketHolder.socket();
		if (mySocket == null) return false;
		
		try {
			mySocket.write(array);
			return true;
		} catch (IOException iox) {
			log(iox);
			_socketHolder.crash(mySocket);
			return false;
		}
	}


	public void setLegacyReceiver(Omnivore<byte[]> receiver) {
		if (_legacyReceiver != null) throw new IllegalStateException();
		_legacyReceiver = receiver;
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
					notifyReceiver(packet);
			}
		}});
	}

	private void notifyReceiver(byte[] packet) {
		byte[] payload = new byte[packet.length - 1];
		byte protocolByte = packet[0];
		
		System.arraycopy(packet, 1, payload, 0, payload.length);
		
		if (protocolByte == LEGACY_PROTOCOL) {
			_legacyReceiver.consume(payload);
			return;
		}

		if (protocolByte == NEW_PROTOCOL) {
			_receiver.consume(payload);
			return;
		}
		
		throw new IllegalStateException("Illegal protocol byte: " + protocolByte);
	}

	private byte[] tryToReceive() {
		ByteArraySocket mySocket = _socketHolder.socket();
		if (mySocket ==  null) return null;
		
		try {
			return mySocket.read();
		} catch (IOException e) {
			log("Connection with peer closed: {} - {}", e.getClass(), e.getMessage());
			_socketHolder.crash(mySocket);
			return null;
		} 

	}

	@Override
	public void send(byte[] payload) {
		byte[] packet = flagWithProtocol(payload, NEW_PROTOCOL);
		while (!tryToSend(packet))
			Threads.sleepWithoutInterruptions(10); //Optimize Use wait/notify.
	}

}
