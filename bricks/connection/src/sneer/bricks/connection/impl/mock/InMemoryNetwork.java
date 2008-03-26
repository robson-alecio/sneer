package sneer.bricks.connection.impl.mock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sneer.bricks.network.ByteArrayServerSocket;
import sneer.bricks.network.ByteArraySocket;
import sneer.bricks.network.Network;
import sneer.bricks.network.impl.ByteArrayServerSocketImpl;
import wheel.io.network.mocks.Permit;

public class InMemoryNetwork implements Network {

	protected Map<Integer, Server> _serverByPort = new HashMap<Integer, Server>();
	
	protected Permit _permit = new Permit();
	
	@Override
	public ByteArrayServerSocket openServerSocket(int port) throws IOException {
		Server server = startServer(port);
		return server.socket();
	}

	@Override
	public ByteArraySocket openSocket(String hostAddress, int port) throws IOException {
		Server server = _serverByPort.get(port);
		if (server == null) throw new IOException("No server is listening on port "+port);
		return server.openClientSocket();
	}
	
	public Server startServer(int port) {
		Server server = _serverByPort.get(port);
		if(server != null) throw new IllegalArgumentException("Port "+port+" already in use.");
		server = new Server(port, _permit);
		_serverByPort.put(port, server);
        return server;
	}
	
    public void crash() {
        _permit.expire();
    }
    

    public void recover() {
        _permit = new Permit();
    }


}



class Server {

	private int _port;

	private Permit _permit;
	
	private ByteArrayServerSocket _serverSocket;
	
	public Server(int port, Permit permit) {
		_port = port;
		_permit = permit;
	}

	public ByteArraySocket openClientSocket() {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	public ByteArrayServerSocket socket() throws IOException {
		if(_serverSocket == null) _serverSocket = new ByteArrayServerSocketMock(_port);
		return _serverSocket;
	}

	
}