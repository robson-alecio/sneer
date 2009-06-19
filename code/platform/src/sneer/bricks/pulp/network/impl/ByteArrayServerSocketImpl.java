//Copyright (C) 2008 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Alexandre Nodari.

package sneer.bricks.pulp.network.impl;

import java.io.IOException;
import java.net.ServerSocket;

import sneer.bricks.pulp.network.ByteArrayServerSocket;
import sneer.bricks.pulp.network.ByteArraySocket;

class ByteArrayServerSocketImpl implements ByteArrayServerSocket {

	private final ServerSocket _serverSocket;

	ByteArrayServerSocketImpl(int port) throws IOException {
		_serverSocket = new ServerSocket(port);
	}

	@Override
	public ByteArraySocket accept() throws IOException {
		return new ByteArraySocketImpl(_serverSocket.accept());
	}

	@Override
	public void crash() {
		try {
			_serverSocket.close();
		} catch (IOException e) {
			//Yes. The correct thing is to do nothing here.
		}
	}

}
