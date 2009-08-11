package sneer.tests.utils.network;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import sneer.bricks.pulp.network.ByteArraySocket;

class InProcessByteArraySocket implements ByteArraySocket {

	private InProcessByteArraySocket _counterpart;
	
	private List<byte[]> _receivedObjects = new LinkedList<byte[]>();
	
	private volatile boolean _isClosed = false;

	private final int _port;

	
	public InProcessByteArraySocket(int port) {
		_port = port;
		initialize(new InProcessByteArraySocket(port, this));
	}

	
	private InProcessByteArraySocket(int port, InProcessByteArraySocket counterpart) {
		_port = port;
		initialize(counterpart);
	}

	
	private void initialize(InProcessByteArraySocket counterpart) {
		_counterpart = counterpart;
	}

	
	@Override
	public void write(byte[] array) throws IOException {
		checkIsOpen();
		
		_counterpart.receive(array);
	}

	
	private synchronized void receive(byte[] array) {
		_receivedObjects.add(array);
		notify();
	}

	
	@Override
	public synchronized byte[] read() throws IOException {
		while (true) {
			checkIsOpen();
			if (!_receivedObjects.isEmpty()) break;
			waitWithoutInterruptions();
		}
		
		return _receivedObjects.remove(0);
	}

	
	public ByteArraySocket counterpart() {
		return _counterpart;
	}

	
	@Override
	public void close() {
		if (_isClosed) return;
		_isClosed = true;
		synchronized (this) { notifyAll(); }
		
		_counterpart.close();
	}
	
	
	private void checkIsOpen() throws IOException {
		if (_isClosed) throw new IOException("This socket was closed.");
	}

	
	private void waitWithoutInterruptions() {
		try {
			wait();
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}		
	}

	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + _port + ")";
	}
	
}