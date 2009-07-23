package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.util.Arrays;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.network.computers.sockets.connections.ByteConnection;
import sneer.bricks.network.computers.sockets.protocol.ProtocolTokens;
import sneer.bricks.network.social.Contact;
import sneer.bricks.pulp.bandwidth.BandwidthCounter;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.lang.Consumer;

class ByteConnectionImpl implements ByteConnection {

	private final Seals _keyManager = my(Seals.class);
	private final Threads _threads = my(Threads.class);
	private final BandwidthCounter _bandwidthCounter = my(BandwidthCounter.class);

	private final String _label;
	private final Contact _contact;

	private final Register<Boolean> _isConnected = my(Signals.class).newRegister(false);
	private final SocketHolder _socketHolder = new SocketHolder(_isConnected.setter());
	
	private PacketScheduler _scheduler;
	private Consumer<byte[]> _receiver;
	
	private Contract _refToAvoidGc;
	private Contract _refToAvoidGc2;


	ByteConnectionImpl(String label, Contact contact) {
		_label = label;
		_contact = contact;
	}

	@Override
	public String toString() {
		return _label + " - " + _contact.nickname();
	}

	
	@Override
	public Signal<Boolean> isConnected() {
		return _isConnected.output();
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
		socket.write(_keyManager.ownSeal().bytes());
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
			crash(mySocket, "Error trying to send packet. ");
			return false;
		}

		_bandwidthCounter.sent(array.length);
		return true;
}


	private void startSending() {
		_refToAvoidGc = _threads.startStepping(new Steppable() { @Override public void step() {
			if (tryToSend(_scheduler.highestPriorityPacketToSend()))
				_scheduler.previousPacketWasSent();
			else
				_threads.sleepWithoutInterruptions(500); //Optimize Use wait/notify.
		}});
	}
	
	private void startReceiving() {
		_refToAvoidGc2 = _threads.startStepping(new Steppable() { @Override public void step() {
			if (!tryToReceive())
				_threads.sleepWithoutInterruptions(500); //Optimize Use wait/notify
		}});
	}

	private boolean tryToReceive() {
		ByteArraySocket mySocket = _socketHolder.socket();
		if (mySocket ==  null) return false;

		byte[] array;
		try {
			array = mySocket.read();
		} catch (Exception e) {
			crash(mySocket, "Error trying to receive packet.");
			return false;
		}

		_bandwidthCounter.received(array.length);
		_receiver.consume(array);
		return true;
	}

	private void crash(ByteArraySocket mySocket, final String message) {
		my(Logger.class).log("Closing socket. {}", message);
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
		_refToAvoidGc.dispose();
		_refToAvoidGc2.dispose();
		
		ByteArraySocket socket = _socketHolder.socket();
		if (socket == null) return;
		crash(socket, "");
	}

}