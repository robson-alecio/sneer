package sneer.pulp.connection.impl;

import static sneer.brickness.environments.Environments.my;
import static wheel.io.Logger.logShort;

import java.io.IOException;
import java.util.Arrays;

import sneer.pulp.bandwidth.BandwidthCounter;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.threadpool.ThreadPool;
import wheel.lang.Consumer;
import wheel.lang.Threads;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class ByteConnectionImpl implements ByteConnection {

	private final KeyManager _keyManager = my(KeyManager.class);
	private final ThreadPool _threadPool = my(ThreadPool.class);
	private final BandwidthCounter _bandwidthCounter = my(BandwidthCounter.class);

	private final String _label;
	private final Contact _contact;

	private final Register<Boolean> _isOnline = new RegisterImpl<Boolean>(false);
	private final SocketHolder _socketHolder = new SocketHolder(_isOnline.setter());
	
	private PacketScheduler _scheduler;
	private Consumer<byte[]> _receiver;


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
		} catch (IOException iox) {
			crash(mySocket, iox, "Error trying to send packet. ");
			return false;
		}

		_bandwidthCounter.sent(array.length);
		return true;
}


	private void startSending() {
		_threadPool.registerActor(new Runnable() { @Override public void run() {
			while (true) {
				if (tryToSend(_scheduler.highestPriorityPacketToSend()))
					_scheduler.previousPacketWasSent();
				else
					Threads.sleepWithoutInterruptions(500); //Optimize Use wait/notify.
			}
		}});
	}
	
	private void startReceiving() {
		_threadPool.registerActor(new Runnable() { @Override public void run() {
			while (true) {
				if (!tryToReceive())
					Threads.sleepWithoutInterruptions(500); //Optimize Use wait/notify
			}
		}});
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
		logShort(e, message);
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

}