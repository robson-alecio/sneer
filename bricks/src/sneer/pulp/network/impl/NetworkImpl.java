package sneer.pulp.network.impl;

import java.io.IOException;

import sneer.pulp.network.ByteArrayServerSocket;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;

class NetworkImpl implements Network {

	@Override
	public ByteArrayServerSocket openServerSocket(int port) throws IOException {
		return new ByteArrayServerSocketImpl(port);
	}

	@Override
	public ByteArraySocket openSocket(String host, int port) throws IOException {
		return new ByteArraySocketImpl(host, port);
	}
}
