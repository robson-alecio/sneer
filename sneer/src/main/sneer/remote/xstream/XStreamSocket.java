package sneer.remote.xstream;

import java.io.IOException;

import wheel.experiments.environment.network.ObjectSocket;

import com.thoughtworks.xstream.XStream;

public class XStreamSocket implements ObjectSocket {
	
	private final ObjectSocket _delegate;
	private final XStream _xStream;

	public XStreamSocket(XStream xstream, ObjectSocket delegate) {
		_xStream = xstream;
		_delegate = delegate;
	}

	public void close() throws IOException {
		_delegate.close();
	}

	public Object readObject() throws IOException, ClassNotFoundException {
		String xml = (String)_delegate.readObject();
		return _xStream.fromXML(xml);
	}

	public void writeObject(Object obj) throws IOException {
		String xml = _xStream.toXML(obj);
		_delegate.writeObject(xml);
	}

	
}
