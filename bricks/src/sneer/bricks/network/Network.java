package sneer.bricks.network;

import java.io.IOException;

import sneer.lego.Brick;


public interface Network extends Brick {

	ByteArraySocket openSocket(String hostAddress, int port) throws IOException;

	ByteArrayServerSocket openServerSocket(int port) throws IOException;

}
