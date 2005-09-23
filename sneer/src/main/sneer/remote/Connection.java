//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sneer.remote;

import java.io.IOException;

import sneer.life.LifeView;
import sneer.life.NoneOfYourBusiness;
import wheel.experiments.environment.network.OldNetwork;


public class Connection implements QueryExecuter {

	private final OldNetwork _network;
	private final String _ipAddress;
	private final int _port;

	private final LifeView _lifeView; 

    private ParallelSocket _socket;
    

    @SuppressWarnings("unchecked")
	public <T> T execute(Query<T> query) throws IOException {
    	
		Object result;
		ParallelSocket mySocket = null;
		try {
	    	synchronized (this) {
	    		if (!isOnline()) goOnline();
				mySocket = _socket;
	    	}
	    	result = mySocket.getReply(query);
		} catch (IOException x) {
			if (_socket == mySocket) _socket = null;
			throw x;
		}
		if (result instanceof NoneOfYourBusiness) throw new NoneOfYourBusiness((NoneOfYourBusiness)result);
		return (T)result;
    }

	private boolean isOnline() {
		return _socket != null;
	}

	private void goOnline() throws IOException {
		_socket = new ParallelSocket(_network.openSocket(_ipAddress, _port));
	}

	public Connection(OldNetwork network, String ipAddress, int port) {
		_network = network;
		_ipAddress = ipAddress;
		_port = port;
		
		_lifeView = new LifeViewProxy(this);
	}

	public LifeView lifeView() {
		return	_lifeView;
	}

}
