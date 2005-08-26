package sneer.remote.xstream;

import java.io.IOException;

import wheel.experiments.environment.network.ObjectServerSocket;
import wheel.experiments.environment.network.ObjectSocket;
import wheel.experiments.environment.network.OldNetwork;

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
