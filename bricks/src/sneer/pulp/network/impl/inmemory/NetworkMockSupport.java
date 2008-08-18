package sneer.pulp.network.impl.inmemory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sneer.pulp.network.ByteArrayServerSocket;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.network.Network;


public abstract class NetworkMockSupport implements Network {
    
	protected final Map<Integer, ByteArrayServerSocket> _serverSocketByPort = new HashMap<Integer, ByteArrayServerSocket>();

    protected void crashIfNotLocal(String serverIpAddress) throws IOException {
        if (!serverIpAddress.equals("localhost")) throw new IOException("Only localhost connections are supported by the NetworkMock. Attempted: " + serverIpAddress);
    }

    protected InMemoryByteArrayServerSocket findServer(int serverPort) {
        return (InMemoryByteArrayServerSocket) _serverSocketByPort.get(new Integer(serverPort));
    }
    
    protected ByteArraySocket startClient(int serverPort) throws IOException {
        InMemoryByteArrayServerSocket server = findServer(serverPort); 
        if (server == null) throw new IOException("No server is listening on this port.");

        return server.openClientSocket();
    }

    protected ByteArrayServerSocket startServer(int serverPort) throws IOException {
        InMemoryByteArrayServerSocket old = findServer(serverPort);
        if (old != null) throw new IOException("Port "+serverPort+" already in use.");

        InMemoryByteArrayServerSocket result = new InMemoryByteArrayServerSocket();
        _serverSocketByPort.put(serverPort, result);
        
        return result;
    }
}
