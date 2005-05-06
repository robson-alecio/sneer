//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Fabio Roger Manera.

package sovereign.remote;

import java.io.IOException;

import org.prevayler.foundation.Cool;
import org.prevayler.foundation.network.ObjectSocket;

import sovereign.Life;
import sovereign.LifeView;

// FIXME: this class could use a more meaningful name
public class LifeResponder implements Runnable {

	private final Life life;
	private LifeView remoteLife;
	private final ObjectSocket socket;
	private boolean startedServing = false;
	
	
	public LifeResponder(Life life, ObjectSocket socket) {
		this.life = life;
		this.socket = socket;
	}
	
	public LifeResponder(Life life, LifeView remoteLife, ObjectSocket socket) {
		this.life = life;
		this.socket = socket;
		setRemoteLife(remoteLife);
	}

	public void setRemoteLife (LifeView remoteLife) {
		this.remoteLife = remoteLife;
		Cool.startDaemon(this);
	}

	public void run() {
		try {
			serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void serve() throws Exception {
		startedServing = true;
		LifeView.CALLING_CONTACT.life(remoteLife);
		while (true) {
			executeQueryOnSocket((Query) socket.readObject());
		}
	}

	private void executeQueryOnSocket(Query sovereignQuery) throws IOException {
		socket.writeObject(sovereignQuery.executeOn(life));
	}
	
	
	public String getServerTicket() {
		if (startedServing) {
			throw new RuntimeException("Cannot call getServerTicket after started the query-execute thread");
		}
		try {
			socket.writeObject(LifeServer.REQUEST_FOR_CLIENT);
			Object object = socket.readObject();
			while (object instanceof Query) {
				executeQueryOnSocket((Query) object);
				object = socket.readObject();
			}
			return (String) object;
		} catch (Exception e) {
			Cool.unexpected(e);
		}
		return null;
	}

}
