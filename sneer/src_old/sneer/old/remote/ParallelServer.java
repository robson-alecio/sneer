//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Rodrigo B de Oliveira.

package sneer.old.remote;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import sneer.old.life.Life;
import wheel.io.network.ObjectServerSocket;
import wheel.io.network.ObjectSocket;
import wheel.lang.Threads;

public class ParallelServer implements Runnable {

	public interface User {
		boolean authorizeConnectionFrom(String name);
	}

	private final Life _life;

	private final ObjectServerSocket _serverSocket;

	private final Set<Long> _sentPeerIds = new HashSet<Long>();

	private final User _user;

	public ParallelServer(Life life, ObjectServerSocket serverSocket, User user) {
		_life = life;
		_serverSocket = serverSocket;
		_user = user;
		Threads.startDaemon(this);
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

			Threads.startDaemon(new Runnable() {
				public void run() {
					try {
						serve(socket);
					} catch (IOException ignored) {
						// The client will reconnect, if this connection was
						// really important. :)
					} catch (ClassNotFoundException cnfx) {
						cnfx.printStackTrace();
					}
				}
			});
		}
	}

	private void serve(final ObjectSocket socket) throws IOException,
			ClassNotFoundException {
		shakeHands(socket);
		
		while (true) {

			final Envelope envelope = (Envelope) socket.readObject();

			Threads.startDaemon(new Runnable() {
				public void run() {
					reply(socket, envelope);
				}
			});
		}
	}

	private void shakeHands(final ObjectSocket socket) throws IOException, ClassNotFoundException {
		Long sentId = (Long)socket.readObject();
		System.out.println("sentId: " + sentId);
		
		if (sentId == null) {
			String name = (String)socket.readObject();
			if (!_user.authorizeConnectionFrom("" + name)) {
				socket.close();
				return;
			}

			socket.writeObject(generateNewId());
			return;
		}
		
		if (_sentPeerIds.contains(sentId)) return;
		
		socket.close();
		throw new IOException("Peer identification failed."); //TODO: Separate security from IOExceptions.
	}

	private Long generateNewId() {
		long result;
		do { result = System.currentTimeMillis(); } while (_sentPeerIds.contains(result));
		_sentPeerIds.add(result);
		return result;
	}

	private void reply(final ObjectSocket socket, final Envelope envelope) {
		Object contents = envelope._contents;

		if (contents instanceof Indian) {
			Indian scout = ((Indian) contents);
			scout.reportAbout(_life, socket);
			envelope._contents = "Indian settled";
		} else {
			Query<?> query = (Query<?>) contents;
			Object result = query.executeOn(_life);
			envelope._contents = result;
		}

		try {
			synchronized (socket) {
				socket.writeObject(envelope);
			}
		} catch (IOException e) {
			// If it was really important, the remote peer will ask again. :)
		}
	}

	public void close() throws IOException {
		_serverSocket.close();
	}

}
