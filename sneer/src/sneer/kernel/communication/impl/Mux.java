package sneer.kernel.communication.impl;

import java.util.HashMap;
import java.util.Map;

import wheel.io.Connection;

public class Mux {

	private final Connection _sharedConnection;
	private final Map<String, Connection> _muxedConnectionsById = new HashMap<String, Connection>();

	public Mux(Connection sharedConnection) {
		_sharedConnection = sharedConnection;
	}

	public Connection muxedConnectionFor(String connectionId) {
		Connection result = _muxedConnectionsById.get(connectionId);
		if (result != null) return result;
		
		result = new MuxedConnection(connectionId, _sharedConnection);
		_muxedConnectionsById.put(connectionId, result);
		return result;
	}

}
