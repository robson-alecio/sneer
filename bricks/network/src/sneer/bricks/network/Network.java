package sneer.bricks.network;

import wheel.io.network.ObjectServerSocket;
import wheel.io.network.ObjectSocket;

public interface Network {

	ObjectSocket openSocket(String serverIpAddress, int serverPort) throws NetworkException;

	ObjectServerSocket openObjectServerSocket(int port) throws NetworkException;

}
