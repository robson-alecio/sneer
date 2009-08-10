package sneer.bricks.network.computers.sockets.connections.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.network.computers.sockets.connections.ByteConnection;
import sneer.bricks.pulp.bandwidth.BandwidthCounter;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.lang.Consumer;

class ByteConnectionImpl implements ByteConnection {

	static private final Threads Threads = my(Threads.class);
	private final BandwidthCounter _bandwidthCounter = my(BandwidthCounter.class);

	private final Register<Boolean> _isConnected = my(Signals.class).newRegister(false);
	private final SocketHolder _socketHolder = new SocketHolder(_isConnected.setter());
	
	private PacketScheduler _scheduler;
	private Consumer<byte[]> _receiver;
	
	private Contract _contractToSend;
	private Contract _contractToReceive;


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

	
	private void startSending() {
		_contractToSend = Threads.startStepping(new Steppable() { @Override public void step() {
			send(_socketHolder.waitForSocket());
		}});
	}

	
	private void startReceiving() {
		_contractToReceive = Threads.startStepping(new Steppable() { @Override public void step() {
			receiveFrom(_socketHolder.waitForSocket());
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
			_socketHolder.close(mySocket, "Error trying to send packet.", iox);
			return false;
		}

		_bandwidthCounter.sent(array.length);
		return true;
	}


	private void receiveFrom(ByteArraySocket mySocket) {
		byte[] array;
		try {
			array = mySocket.read();
		} catch (IOException iox) {
			_socketHolder.close(mySocket, "Error trying to receive packet.", iox);
			return;
		}

		_bandwidthCounter.received(array.length);
		_receiver.consume(array);
	}

	
	void close() {
		_contractToSend.dispose();
		_contractToReceive.dispose();
		
		_socketHolder.close("Connection closed.");
	}


	SocketHolder socketHolder() {
		return _socketHolder;
	}

}