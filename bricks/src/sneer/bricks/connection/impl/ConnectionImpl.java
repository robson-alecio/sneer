package sneer.bricks.connection.impl;

import java.io.IOException;
import java.util.Arrays;

import sneer.bricks.connection.Connection;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.log.Logger;
import sneer.bricks.network.ByteArraySocket;
import sneer.lego.Inject;
import wheel.lang.Omnivore;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class ConnectionImpl implements Connection {

	@Inject
	private KeyManager _keyManager;
	
	private Register<Boolean> _isOnline = new RegisterImpl<Boolean>(false);

	private final SocketHolder _socketHolder = new SocketHolder(_isOnline.setter());

	@Inject
	private Logger _logger;

	private Omnivore<byte[]> _receiver;
	
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
		socket.write(_keyManager.ownPublicKey());
		byte[] response = socket.read();
		return Arrays.equals(ProtocolTokens.OK, response);
	}

	void manageIncomingSocket(ByteArraySocket socket) {
		//Implement: Reject if my own pk.
		//Challenge pk.
		
		_socketHolder.setSocketIfNecessary(socket);
		
	}
	
	@Override
	public boolean tryToSend(byte[] array) {
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
		_receiver = receiver;
	}
}
