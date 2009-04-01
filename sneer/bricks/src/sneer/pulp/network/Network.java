package sneer.pulp.network;

import java.io.IOException;

import sneer.brickness.OldBrick;


public interface Network extends OldBrick {

	ByteArraySocket openSocket(String hostAddress, int port) throws IOException;

	ByteArrayServerSocket openServerSocket(int port) throws IOException;

}
