//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sneer.remote;

import java.io.IOException;

import sneer.life.Life;
import wheel.experiments.Cool;
import wheel.experiments.environment.network.ObjectServerSocket;
import wheel.experiments.environment.network.ObjectSocket;


public class ParallelServer implements Runnable {

	private final Life _life;
	private final ObjectServerSocket _serverSocket;

	public ParallelServer(Life life, ObjectServerSocket serverSocket) {
		_life = life;
		_serverSocket = serverSocket;
		Cool.startDaemon(this);
	}

    public void run() {
		while (true) {
			final ObjectSocket socket;
			
			try {
				socket = _serverSocket.accept();
			} catch (IOException iox) {
				iox.printStackTrace();
				break;
			}

			Cool.startDaemon(new Runnable() {
				public void run() {
					try {
						serve(socket);
					} catch (IOException ignored) {
						//The client will reconnect, if this connection was really important. :)
					} catch (ClassNotFoundException cnfx) {
						cnfx.printStackTrace();
					}
				}
			});
		}
	}

	private void serve(final ObjectSocket socket) throws IOException, ClassNotFoundException {
		while (true) {
			
			final Envelope envelope = (Envelope)socket.readObject();
			
			Cool.startDaemon(new Runnable() {
				public void run() {
					reply(socket, envelope);
				}
			});
		}
	}

	private void reply(final ObjectSocket socket, final Envelope envelope) {
		Query query = (Query)envelope.contents();
		Object result = query.executeOn(_life);
		envelope.contents(result);
		try {
			synchronized (socket) {
				socket.writeObject(envelope);
			}
		} catch (IOException e) {
			//If it was really important, the remote peer will ask again.  :)
		}
	}

	public void close() throws IOException {
		_serverSocket.close();
	}

	
}
