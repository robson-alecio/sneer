package spikes.wheel.io.network.impl;

import java.io.IOException;

import spikes.wheel.io.network.ObjectSocket;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

public class XStreamSocket implements ObjectSocket {
	
	private final ObjectSocket _delegate;
	private final XStream _xStream;
	
	private final Object _readMonitor = new Object();
	private final Object _writeMonitor = new Object();
	
	public XStreamSocket(XStream xstream, ObjectSocket delegate) {
		_xStream = xstream;
		_delegate = delegate;
	}

	public void close() throws IOException {
		_delegate.close();
	}

	public Object readObject() throws IOException, ClassNotFoundException {
		synchronized (_readMonitor) {
			String xml;
			try {
				xml = (String)_delegate.readObject();
			} catch (ClassNotFoundException e) {
				throw new IOException("XStream sockets should only carry Strings.");
			} catch (ClassCastException e) {
				throw new IOException("XStream sockets should only carry Strings.");
			}
			try {
				return _xStream.fromXML(xml);
			} catch (ConversionException e) {
				if (e.getCause() instanceof CannotResolveClassException)
					throw new ClassNotFoundException(e.getMessage());
				throw e;
			}
		}
	}

	public void writeObject(Object obj) throws IOException {
		synchronized (_writeMonitor) {
			String xml = _xStream.toXML(obj);
			_delegate.writeObject(xml);
		}
	}
	
}
