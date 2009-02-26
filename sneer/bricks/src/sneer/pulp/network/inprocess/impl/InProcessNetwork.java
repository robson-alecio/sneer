package sneer.pulp.network.inprocess.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sneer.pulp.network.ByteArrayServerSocket;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;

public class InProcessNetwork implements Network {

	protected final Map<Integer, ByteArrayServerSocket> _serverSocketByPort = new HashMap<Integer, ByteArrayServerSocket>();

	@Override
	public synchronized ByteArraySocket openSocket(String serverIpAddress, int serverPort) throws IOException {
		crashIfNotLocal(serverIpAddress);
		return startClient(serverPort);
	}

	@Override
	public ByteArrayServerSocket openServerSocket(int port) throws IOException {
		return startServer(port);
	}

	protected void crashIfNotLocal(String serverIpAddress) throws IOException {
	    if (!serverIpAddress.equals("localhost")) throw new IOException("Only localhost connections are supported by the NetworkMock. Attempted: " + serverIpAddress);
	}

	protected InProcessByteArrayServerSocket findServer(int serverPort) {
	    return (InProcessByteArrayServerSocket) _serverSocketByPort.get(new Integer(serverPort));
	}

	protected ByteArraySocket startClient(int serverPort) throws IOException {
	    InProcessByteArrayServerSocket server = findServer(serverPort); 
	    if (server == null) throw new IOException("No server is listening on this port.");
	
	    return server.openClientSocket();
	}

	protected ByteArrayServerSocket startServer(int serverPort) throws IOException {
	    InProcessByteArrayServerSocket old = findServer(serverPort);
	    if (old != null) throw new IOException("Port "+serverPort+" already in use.");
	
	    InProcessByteArrayServerSocket result = new InProcessByteArrayServerSocket();
	    _serverSocketByPort.put(serverPort, result);
	    
	    return result;
	}
}