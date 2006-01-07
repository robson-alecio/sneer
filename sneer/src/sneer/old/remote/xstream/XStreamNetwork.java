package sneer.old.remote.xstream;

import java.io.IOException;

import wheelexperiments.environment.network.ObjectServerSocket;
import wheelexperiments.environment.network.ObjectSocket;
import wheelexperiments.environment.network.OldNetwork;

import com.thoughtworks.xstream.XStream;

public class XStreamNetwork implements OldNetwork {

	private final XStream _xStream;
	private final OldNetwork _delegate;

	public XStreamNetwork(XStream xStream, OldNetwork delegate) {
		_xStream = xStream;
		_delegate = delegate;
	}

	public ObjectSocket openSocket(String host, int port) throws IOException {
		return new XStreamSocket(_xStream, _delegate.openSocket(host, port));
	}

	public ObjectServerSocket openObjectServerSocket(int port)
			throws IOException {
		return new XStreamServerSocket(_xStream, _delegate.openObjectServerSocket(port));
	}

}
