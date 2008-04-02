package sneer.bricks.network.impl.inmemory;

import java.io.IOException;

import sneer.bricks.network.ByteArrayServerSocket;
import sneer.bricks.network.ByteArraySocket;

public class InMemoryNetwork extends NetworkMockSupport {

	@Override
	public synchronized ByteArraySocket openSocket(String serverIpAddress, int serverPort) throws IOException {
		crashIfNotLocal(serverIpAddress);
		return startClient(serverPort);
	}

	@Override
	public ByteArrayServerSocket openServerSocket(int port) throws IOException {
		return startServer(port);
	}
}