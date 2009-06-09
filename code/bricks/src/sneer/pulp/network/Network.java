package sneer.pulp.network;

import java.io.IOException;

import sneer.brickness.Brick;

@Brick
public interface Network{

	ByteArraySocket openSocket(String hostAddress, int port) throws IOException;

	ByteArrayServerSocket openServerSocket(int port) throws IOException;

}
