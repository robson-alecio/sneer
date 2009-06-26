package sneer.tests.utils.network;

import java.io.IOException;

import sneer.bricks.pulp.network.ByteArrayServerSocket;
import sneer.bricks.pulp.network.ByteArraySocket;

class InProcessByteArrayServerSocket implements ByteArrayServerSocket {

	private ByteArraySocket _clientSide;

	public synchronized ByteArraySocket accept() throws IOException {
		
		if (_clientSide != null) throw new IOException("Port already in use.");
		InProcessByteArraySocket result = new InProcessByteArraySocket();
		_clientSide = result.counterpart();
		
		notifyAll(); //Notifies all client threads.
		waitWithoutInterruptions();

		return result;
	}

	synchronized ByteArraySocket openClientSocket() {
		while (_clientSide == null)
			waitWithoutInterruptions();

		ByteArraySocket result = _clientSide;
        _clientSide = null;
        notifyAll(); //Notifies the server thread (necessary) and eventual client threads (harmless).
        return result;
	}

	@Override
	public void crash() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet();
	}
	
	private void waitWithoutInterruptions() {
		try {
			wait();
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}		
	}

}