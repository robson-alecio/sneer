package sneer.old.remote.xstream;

import java.io.IOException;

import wheelexperiments.environment.network.ObjectServerSocket;
import wheelexperiments.environment.network.ObjectSocket;

import com.thoughtworks.xstream.XStream;

public class XStreamServerSocket implements ObjectServerSocket {

	private final XStream _xStream;
	private final ObjectServerSocket _delegate;

	public XStreamServerSocket(XStream xStream, ObjectServerSocket delegate) {
		_xStream = xStream;
		_delegate = delegate;
	}

	public ObjectSocket accept() throws IOException {
		return new XStreamSocket(_xStream, _delegate.accept());
	}

	public void close() throws IOException {
		_delegate.close();
	}


}
