//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sneer.remote;

import java.io.IOException;

import org.prevayler.foundation.Cool;
import org.prevayler.foundation.network.ObjectSocket;
import org.prevayler.foundation.network.OldNetwork;

import sneer.life.LifeView;
import sneer.life.NoneOfYourBusiness;


public class Connection implements QueryExecuter {

	private LifeView _lifeView = LifeViewProxy.createBackedBy(this); 

	private final OldNetwork _network;
	private final String _ipAddress;
	private final int _port;

    private ParallelSocket _socket;

    public Object execute(Query query) {
		Object result;
		ParallelSocket mySocket = null;
		try {
	    	synchronized (this) {
	    		while (mySocket == null) {
	    			goOnline();
					mySocket = _socket;
	    		}
	    	}
	    	result = mySocket.getReply(query);
		} catch (Exception x) {
			if (_socket == mySocket) _socket = null;
			throw new RuntimeException(x);
		}
		if (result instanceof NoneOfYourBusiness) throw new NoneOfYourBusiness((NoneOfYourBusiness)result);
		return result;
    }

	public boolean isOnline() {
		return _socket != null;
	}

	private void goOnline() throws IOException {
		_socket = new ParallelSocket(_network.openSocket(_ipAddress, _port));
	}

	public Connection(OldNetwork network, String ipAddress, int port) {
		_network = network;
		_ipAddress = ipAddress;
		_port = port;
		
		Cool.startDaemon(watchDog());
	}
	
	private Runnable watchDog() {
		return new Runnable() {
			public void run() {
				while (true) {
					ping();
					Cool.sleep(1000 * 60); //TODO Optimize - Sleep again instead of pinging, if this connection was used recently.
				}
			}
			
			private void ping() {
				try {
					// force a remote query to be executed (see execute method above)
					_lifeView.name();
				} catch (RuntimeException provoked) {
					//This exception already caused this connection to go offline on its own.
				}
			}
		};
	}

	public LifeView lifeView() {
		return	_lifeView;
	}

}
