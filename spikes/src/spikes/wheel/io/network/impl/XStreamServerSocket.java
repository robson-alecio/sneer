package spikes.wheel.io.network.impl;

import java.io.IOException;

import spikes.wheel.io.network.ObjectServerSocket;
import spikes.wheel.io.network.ObjectSocket;


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

	public void close() {
		_delegate.close();
	}


}
