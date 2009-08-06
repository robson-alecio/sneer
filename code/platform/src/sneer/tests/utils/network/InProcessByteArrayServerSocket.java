package sneer.tests.utils.network;

import java.io.IOException;

import sneer.bricks.pulp.network.ByteArrayServerSocket;
import sneer.bricks.pulp.network.ByteArraySocket;

class InProcessByteArrayServerSocket implements ByteArrayServerSocket {

	private ByteArraySocket _clientSide;
	private volatile boolean _isCrashed;
	private final int _serverPort;

	public InProcessByteArrayServerSocket(int serverPort) {
		_serverPort = serverPort;
	}

	public synchronized ByteArraySocket accept() throws IOException {
		
		if (_clientSide != null) throw new IOException("Port already in use.");
		InProcessByteArraySocket result = new InProcessByteArraySocket(_serverPort);
		_clientSide = result.counterpart();
		
		notifyAll(); //Notifies all client threads.
		waitWithoutInterruptionsCheckingForCrash();
		
		return result;
	}

	synchronized ByteArraySocket openClientSocket() throws IOException {
		while (_clientSide == null)
			waitWithoutInterruptionsCheckingForCrash();

		ByteArraySocket result = _clientSide;
        _clientSide = null;
        notifyAll(); //Notifies the server thread (necessary) and eventual client threads (harmless).
        return result;
	}
	
	boolean isCrashed() {
		return _isCrashed;
	}

	@Override
	public synchronized void crash() {
		_isCrashed = true;
		notifyAll();
	}
	
	private void waitWithoutInterruptions() {
		try {
			wait();
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}		
	}
	
	private void waitWithoutInterruptionsCheckingForCrash() throws IOException {
		waitWithoutInterruptions();
		if (_isCrashed) throw new IOException("Crashed!");
	}

}