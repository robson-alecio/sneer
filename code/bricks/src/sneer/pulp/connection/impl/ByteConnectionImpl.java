package sneer.pulp.connection.impl;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;
import java.util.Arrays;

import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.bandwidth.BandwidthCounter;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.log.Logger;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.reactive.Register;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.threads.Stepper;
import sneer.pulp.threads.Threads;

class ByteConnectionImpl implements ByteConnection {

	private final KeyManager _keyManager = my(KeyManager.class);
	private final Threads _threads = my(Threads.class);
	private final BandwidthCounter _bandwidthCounter = my(BandwidthCounter.class);

	private final String _label;
	private final Contact _contact;

	private final Register<Boolean> _isOnline = my(Signals.class).newRegister(false);
	private final SocketHolder _socketHolder = new SocketHolder(_isOnline.setter());
	
	private PacketScheduler _scheduler;
	private Consumer<byte[]> _receiver;
	
	private volatile boolean _isClosed;
	private Stepper _refToAvoidGc;


	ByteConnectionImpl(String label, Contact contact) {
		_label = label;
		_contact = contact;
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
			my(Logger.class).logShort(e, "Exception while shaking hands in outgoing connection.");
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
		} catch (IOException iox) {
			crash(mySocket, iox, "Error trying to send packet. ");
			return false;
		}

		_bandwidthCounter.sent(array.length);
		return true;
}


	private void startSending() {
		_threads.registerStepper(new Stepper() { @Override public boolean step() {
			if (tryToSend(_scheduler.highestPriorityPacketToSend()))
				_scheduler.previousPacketWasSent();
			else
				_threads.sleepWithoutInterruptions(500); //Optimize Use wait/notify.
			
			return !_isClosed;
		}});
	}
	
	private void startReceiving() {
		_refToAvoidGc = new Stepper() { @Override public boolean step() {
			if (!tryToReceive())
				_threads.sleepWithoutInterruptions(500); //Optimize Use wait/notify
			return !_isClosed;
		}};

		_threads.registerStepper(_refToAvoidGc);
	}

	private boolean tryToReceive() {
		ByteArraySocket mySocket = _socketHolder.socket();
		if (mySocket ==  null) return false;

		byte[] array;
		try {
			array = mySocket.read();
		} catch (Exception e) {
			crash(mySocket, e, "Error trying to receive packet.");
			return false;
		}

		_bandwidthCounter.received(array.length);
		_receiver.consume(array);
		return true;
	}

	private void crash(ByteArraySocket mySocket, Exception e, final String message) {
		my(Logger.class).logShort(e, message);
		_socketHolder.crash(mySocket);
	}

	@Override
	public void initCommunications(PacketScheduler sender, Consumer<byte[]> receiver) {
		if (_scheduler != null) throw new IllegalStateException();
		_scheduler = sender;
		_receiver = receiver;

		startSending();
		startReceiving();
	}

	void close() {
		_isClosed = true;
		
		ByteArraySocket socket = _socketHolder.socket();
		if (socket == null) return;
		crash(socket, null, "Connection closed");
	}

}