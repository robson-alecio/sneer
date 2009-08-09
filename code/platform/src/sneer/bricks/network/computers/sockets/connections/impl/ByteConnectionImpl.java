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
	
	private Contract _contractToSend;
	private Contract _contractToReceive;


	ByteConnectionImpl(String label, Contact contact) {
		_label = label;
		_contact = contact;
	}

	
	@Override
	public Signal<Boolean> isConnected() {
		return _isConnected.output();
	}


	@Override
	public void initCommunications(PacketScheduler sender, Consumer<byte[]> receiver) {
		if (_scheduler != null) throw new IllegalStateException();
		_scheduler = sender;
		_receiver = receiver;

		startSending();
		startReceiving();
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
			my(Logger.class).log("Exception while shaking hands in outgoing connection: {}", e);
			socket.crash();
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
		if (!_socketHolder.setSocketIfNecessary(socket)) return;
		tryToSend(ProtocolTokens.OK, socket);
	}

	
	private void startSending() {
		_contractToSend = _threads.startStepping(new Steppable() { @Override public void step() {
			send(_socketHolder.waitForSocket());
		}});
	}

	
	private void send(ByteArraySocket socket) {
		if (tryToSend(_scheduler.highestPriorityPacketToSend(), socket))
			_scheduler.previousPacketWasSent();
	}


	private boolean tryToSend(byte[] array, ByteArraySocket mySocket) {
		try {
			mySocket.write(array);
		} catch (IOException iox) {
			crash(mySocket, "Error trying to send packet. ");
			return false;
		}

		_bandwidthCounter.sent(array.length);
		return true;
	}


	private void startReceiving() {
		_contractToReceive = _threads.startStepping(new Steppable() { @Override public void step() {
			receiveFrom(_socketHolder.waitForSocket());
		}});
	}

	
	private void receiveFrom(ByteArraySocket mySocket) {
		byte[] array;
		try {
			array = mySocket.read();
		} catch (IOException e) {
			crash(mySocket, "Error trying to receive packet.");
			return;
		}

		_bandwidthCounter.received(array.length);
		_receiver.consume(array);
	}

	
	private void crash(ByteArraySocket mySocket, final String message) {
		my(Logger.class).log("Closing socket. {}", message);
		_socketHolder.crash(mySocket);
	}

	
	void close() {
		_contractToSend.dispose();
		_contractToReceive.dispose();
		
		ByteArraySocket socket = _socketHolder.socket();
		if (socket == null) return;
		crash(socket, "");
	}

	
	@Override
	public String toString() {
		return _label + " - " + _contact.nickname();
	}

}