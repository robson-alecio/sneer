package sneer.bricks.network.impl;

import java.io.IOException;

import sneer.bricks.network.ByteArrayServerSocket;
import sneer.bricks.network.ByteArraySocket;
import sneer.bricks.network.Network;

public class NetworkImpl implements Network {

	@Override
	public ByteArrayServerSocket openServerSocket(int port) throws IOException {
		return new ByteArrayServerSocketImpl(port);
	}

	@Override
	public ByteArraySocket openSocket(String host, int port) throws IOException {
		return new ByteArraySocketImpl(host, port);
	}
}
