package spikes.wheel.io.network.impl;

import java.io.IOException;

import spikes.wheel.io.network.ObjectServerSocket;
import spikes.wheel.io.network.ObjectSocket;
import spikes.wheel.io.network.OldNetwork;


import com.thoughtworks.xstream.XStream;

public class XStreamNetwork implements OldNetwork {

	private final XStream _xStream = new XStream();
	private final OldNetwork _delegate;

	public XStreamNetwork(OldNetwork delegate) {
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
