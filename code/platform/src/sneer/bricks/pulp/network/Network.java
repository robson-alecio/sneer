package sneer.bricks.pulp.network;

import java.io.IOException;

import sneer.foundation.brickness.Brick;

@Brick
public interface Network{

	ByteArraySocket openSocket(String hostAddress, int port) throws IOException;

	ByteArrayServerSocket openServerSocket(int port) throws IOException;

}
