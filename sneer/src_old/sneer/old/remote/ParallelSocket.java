package sneer.old.remote;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wheel.io.network.ObjectSocket;
import wheel.lang.Threads;

class ParallelSocket {

	private final ObjectSocket _delegate;
	private final Object _writeMonitor = new Object();
	private final Object _readMonitor = new Object();

	private int _stamp = 1;
	private Envelope _envelopeRead = null;
	
	private String _reasonForClosing = null;
	private final POBox _poBox = new POBox();
	
	private final Map<Integer, Indian> _indians = new HashMap<Integer, Indian>();

	ParallelSocket(ObjectSocket delegate) {
		_delegate = delegate;
		Threads.startDaemon(new Runnable() {
			public void run() {
				while (readObject());
			}
		});
	}

	private boolean readObject() {
		try {
			try {
				Object object = _delegate.readObject();
				if (object instanceof Envelope) {
					_poBox.add((Envelope)object);
					return true;
				}
				SmokeSignal smokeSignal = (SmokeSignal)object;  //FIXME Class cast Exceptions are possible if other side is malicious.
				Indian indian = _indians.get(smokeSignal._indianId);
				indian.receive(smokeSignal);
				return true;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new IOException("ClassNotFoundException thrown");
			}
			//FIXME: Check whether this is a valid envelope to prevent all threads from waiting forever.
		} catch (IOException x) {
			closeBecauseOf(x);
			synchronized (_poBox) {
				_poBox.notifyAll();
			}
			return false;
		}
	}

	public Object getReply(Object request) throws IOException {
		
		checkForIndian(request);
		
		Envelope myEnvelope;
		synchronized (_writeMonitor) {
			myEnvelope = new Envelope(request, _stamp++);
			_delegate.writeObject(myEnvelope);
		}
		synchronized (_readMonitor) {
			try {
				while (true) {
					checkOpen();
					if (_envelopeRead == null) _envelopeRead = _poBox.remove();
					if (_envelopeRead._stamp == myEnvelope._stamp) {
						Object result = _envelopeRead._contents;
						_envelopeRead = null;
						return result;
					}
					Threads.waitWithoutInterruptions(_readMonitor);
				}
			} finally {
				_readMonitor.notifyAll();
			}
		}
	}

	private void checkForIndian(Object request) {
    	if (!(request instanceof Indian)) return;
    		Indian indian = (Indian)request;
			_indians.put(indian.id(), indian);
	}

	private void checkOpen() throws IOException {
		if (_reasonForClosing != null) throw new IOException("Socket closed due to " + _reasonForClosing);
	}

	private void closeBecauseOf(Exception exception) {
		try {
			_delegate.close();
		} catch (IOException ignored) {
			//Exceptional socket state is already being treated.
		}
		_reasonForClosing = exception.getClass().toString() + " - " + exception.getMessage();
	}
	
	private class POBox {

	    List<Envelope> _buffer = new LinkedList<Envelope>();

	    synchronized void add(Envelope d) {
	    	_buffer.add(d);
			notify();
		}

	    synchronized Envelope remove() throws IOException {
	       if (_buffer.isEmpty()) Threads.waitWithoutInterruptions(this);
	       checkOpen();
	       return _buffer.remove(0);
	    }

	}
	
}








