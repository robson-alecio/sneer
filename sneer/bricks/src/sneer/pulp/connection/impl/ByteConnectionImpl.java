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

	private static final byte LEGACY_PROTOCOL = 0;
	private static final byte NEW_PROTOCOL = 1;

	@Inject
	static private KeyManager _keyManager;
	
	@Inject
	static private ThreadPool _threadPool;
	
	private final Register<Boolean> _isOnline = new RegisterImpl<Boolean>(false);

	private final SocketHolder _socketHolder = new SocketHolder(_isOnline.setter());
	
	private volatile PacketScheduler _sender;

	private final String _label;

	private final Contact _contact;
	
	private volatile Omnivore<byte[]> _legacyReceiver;
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
			_socketHolder.crash(mySocket);
			logShort(iox, "Error trying to send packet. ");
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
	
	private void startSending() {
		_threadPool.registerActor(new Runnable() { @Override public void run() {
			while (true) {
				byte[] packet = flagWithProtocol(waitForNextPacket(), NEW_PROTOCOL);
				if (tryToSend(packet))
					_sender.lastRequestedPacketWasSent();
				else
					Threads.sleepWithoutInterruptions(10); //Optimize Use wait/notify.
			}
		}});
	}
	
	private byte[] waitForNextPacket() {
		while (true) {
			if (_sender == null) { //Fix: When the old protocol dies, the _sender should never be null. It no longer needs to be volatile either. 
				Threads.sleepWithoutInterruptions(10);
				continue;
			}
			return _sender.currentPacketToSend();
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

	private void notifyReceiver(byte[] packet) throws Exception { //Refactor This Exception will go away once all legacy transmission is moved to the new protocol.
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
		
		throw new Exception("Illegal protocol byte: " + protocolByte);
	}

	private boolean tryToReceive() {
		ByteArraySocket mySocket = _socketHolder.socket();
		if (mySocket ==  null) return false;

		try {
			notifyReceiver(mySocket.read());
			return true;
		} catch (Exception e) {
			logShort(e, "Error trying to receive packet.");
			_socketHolder.crash(mySocket);
			return false;
		} 
	}

	@Override
	public void setSender(PacketScheduler sender) {
		if (_sender != null) throw new IllegalStateException();
		_sender = sender;
	}

}