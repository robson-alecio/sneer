package sneer.bricks.network;

import java.io.IOException;


public interface Network {

	ByteArraySocket openSocket(String hostAddress, int port) throws IOException;

	ByteArrayServerSocket openServerSocket(int port) throws IOException;

}
