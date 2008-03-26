package sneer.bricks.connection.impl.mock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sneer.bricks.network.ByteArrayServerSocket;
import sneer.bricks.network.ByteArraySocket;
import sneer.bricks.network.Network;
import wheel.lang.Threads;


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
        try {
            return server.openClientSocket();
        } catch (IOException e) {
            Threads.sleepWithoutInterruptions(5);
            return server.openClientSocket(); //TODO Eliminate this retry because client must try and reconnect anyway.
        }
    }

    protected ByteArrayServerSocket startServer(int serverPort) throws IOException {
        InMemoryByteArrayServerSocket old = findServer(serverPort);
        if (old != null) throw new IOException("Port "+serverPort+" already in use.");

        InMemoryByteArrayServerSocket result = new InMemoryByteArrayServerSocket();
        _serverSocketByPort.put(serverPort, result);
        
        return result;
    }
}
