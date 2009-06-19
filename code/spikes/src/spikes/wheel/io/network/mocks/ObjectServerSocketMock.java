//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.
//Contributions: Alexandre Nodari.

package spikes.wheel.io.network.mocks;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.hardware.cpu.threads.Threads;
import spikes.wheel.io.network.ObjectServerSocket;
import spikes.wheel.io.network.ObjectSocket;

public class ObjectServerSocketMock implements ObjectServerSocket {

	private ObjectSocket _clientSide;
	private final Permit _permit;


	public ObjectServerSocketMock(Permit permit) {
		_permit = permit;
		_permit.addObjectToNotify(this);
	}

	public synchronized ObjectSocket accept() throws IOException {
		_permit.check();
		
		if (_clientSide != null) throw new IOException("Port already in use.");
		ObjectSocketMock result = new ObjectSocketMock(_permit);
		_clientSide = result.counterpart();
		
		notifyAll(); //Notifies all client threads.
		my(Threads.class).waitWithoutInterruptions(this);

		_permit.check();
		return result;
	}

	synchronized ObjectSocket openClientSocket() throws IOException {
		_permit.check();
		while (_clientSide == null) my(Threads.class).waitWithoutInterruptions(this);
		_permit.check();

        ObjectSocket result = _clientSide;
        _clientSide = null;
        notifyAll(); //Notifies the server thread (necessary) and eventual client threads (harmless).
        return result;
	}

	public void close() {
		// TODO Auto-generated method stub
	}

}
