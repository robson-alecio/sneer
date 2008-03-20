package sneer.bricks.network.impl;

import java.io.IOException;

import sneer.bricks.network.Network;
import sneer.bricks.network.NetworkException;
import wheel.io.network.ObjectServerSocket;
import wheel.io.network.ObjectServerSocketImpl;
import wheel.io.network.ObjectSocket;
import wheel.io.network.ObjectSocketImpl;

public class NetworkImpl implements Network {

	@Override
	public ObjectServerSocket openObjectServerSocket(int port) throws NetworkException {
		try {
			return new ObjectServerSocketImpl(port);
		} catch (IOException e) {
			// Implement Auto-generated catch block
			throw new NetworkException(e);
		}
	}

	@Override
	public ObjectSocket openSocket(String serverIpAddress, int serverPort) throws NetworkException {
		try {
			return new ObjectSocketImpl(serverIpAddress, serverPort);
		} catch (IOException e) {
			throw new NetworkException(e);
		}
	}
}
