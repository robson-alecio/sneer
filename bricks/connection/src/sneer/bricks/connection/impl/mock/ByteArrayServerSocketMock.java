package sneer.bricks.connection.impl.mock;

import java.io.IOException;

import sneer.bricks.network.ByteArrayServerSocket;
import sneer.bricks.network.ByteArraySocket;

public class ByteArrayServerSocketMock implements ByteArrayServerSocket {

	public ByteArrayServerSocketMock(int port) {
	}

	@Override
	public ByteArraySocket accept() throws IOException {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void crash() {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

}
