package sneer.remote.xstream;

import java.io.IOException;

import wheel.experiments.environment.network.ObjectServerSocket;
import wheel.experiments.environment.network.ObjectSocket;

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
