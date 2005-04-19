//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sovereign.remote;

import org.prevayler.foundation.Cool;
import org.prevayler.foundation.network.ObjectServerSocket;
import org.prevayler.foundation.network.ObjectSocket;

import sovereign.Life;

public class LifeServer implements Runnable {

	private final Life _life;
	private final ObjectServerSocket _serverSocket;

	public LifeServer(Life life, ObjectServerSocket serverSocket) {
		_life = life;
		_serverSocket = serverSocket;
		Cool.startDaemon(this);
	}

    public void run() {
		while (true)
			try {
				final ObjectSocket socket = _serverSocket.accept();
				Cool.startDaemon(new Runnable() {
					public void run() {
						try {
							serve(socket);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	private void serve(ObjectSocket socket) throws Exception {
		while (true) {
			Query sovereignQuery = (Query)socket.readObject();
			socket.writeObject(sovereignQuery.executeOn(_life));
		}
	}
	
}
