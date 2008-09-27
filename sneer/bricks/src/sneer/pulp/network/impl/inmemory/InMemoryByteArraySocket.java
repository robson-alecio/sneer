package sneer.pulp.network.impl.inmemory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import sneer.pulp.network.ByteArraySocket;
import wheel.lang.Threads;


public class InMemoryByteArraySocket implements ByteArraySocket {

	private InMemoryByteArraySocket _counterpart;
	
	private List<byte[]> _receivedObjects = new LinkedList<byte[]>();
	
	private volatile boolean _isCrashed = false;
	

	public InMemoryByteArraySocket() {
		initialize(new InMemoryByteArraySocket(this));
	}

	private InMemoryByteArraySocket(InMemoryByteArraySocket counterpart) {
		initialize(counterpart);
	}

	private void initialize(InMemoryByteArraySocket counterpart) {
		_counterpart = counterpart;
	}

	@Override
	public void write(byte[] array) throws IOException {
		checkIsNotCrashed();
		
		_counterpart.receive(array);
	}

	private synchronized void receive(byte[] array) {
		_receivedObjects.add(array);
		notify();
	}

	@Override
	public synchronized byte[] read() throws IOException {
		checkIsNotCrashed();
		
		if (_receivedObjects.isEmpty()) Threads.waitWithoutInterruptions(this);
		return _receivedObjects.remove(0);
	}

	public ByteArraySocket counterpart() {
		return _counterpart;
	}

	@Override
	public void crash() {
		_isCrashed = true;
	}

	private void checkIsNotCrashed() throws IOException {
		if (_isCrashed) throw new IOException("This socket was crashed.");
	}


}