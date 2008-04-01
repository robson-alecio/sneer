package sneer.bricks.connection.impl;

import java.io.IOException;
import java.util.Arrays;

import sneer.bricks.connection.Connection;
import sneer.bricks.keymanager.KeyManager;
import sneer.bricks.mesh.impl.Packet;
import sneer.bricks.network.ByteArraySocket;
import sneer.lego.Brick;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.Register;
import wheel.reactive.Signal;
import wheel.reactive.impl.RegisterImpl;

class ConnectionImpl implements Connection {

	@Brick
	private KeyManager _keyManager;
	
	private Register<Boolean> _isOnline = new RegisterImpl<Boolean>(false);

	private ByteArraySocket _socket;
	
	@Override
	public Signal<Boolean> isOnline() {
		return _isOnline.output();
	}
	
	void manageOutgoingSocket(ByteArraySocket newSocket) {
		synchronized (this){
			if (_socket != null){
				newSocket.crash();
				return;
			}
		}
		
		try {
			if (!shakeHands(newSocket)) {
				newSocket.crash();
				return;
			}
		} catch (IOException e) {
			newSocket.crash();
			return;
		}
		
		setSocket(newSocket);
		
	}

	private boolean shakeHands(ByteArraySocket socket) throws IOException {
		socket.write(ProtocolTokens.SNEER_WIRE_PROTOCOL_1);
		socket.write(_keyManager.ownPublicKey());
		byte[] response = socket.read();
		return Arrays.equals(ProtocolTokens.OK, response);
	}

	void manageIncomingSocket(ByteArraySocket socket) {
		//Implement: Reject if my own pk.
		//Challenge pk.
		
		setSocket(socket);
		
	}
	
	private synchronized void setSocket(ByteArraySocket newSocket) {
		if (_socket != null){
			newSocket.crash();
			return;
		}
		
		_socket = newSocket;
		_isOnline.setter().consume(true);
	}

	private synchronized void crashSocket() {
		_socket.crash();
		_socket = null;
		
		_isOnline.setter().consume(false);
	}

	@Override
	public void send(byte[] array) {
		throw new NotImplementedYet();
	}
}
