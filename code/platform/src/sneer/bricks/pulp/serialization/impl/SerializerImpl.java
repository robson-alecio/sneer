package sneer.bricks.pulp.serialization.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sneer.bricks.pulp.serialization.Serializer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.binary.BinaryStreamReader;
import com.thoughtworks.xstream.io.binary.BinaryStreamWriter;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

class SerializerImpl implements Serializer {

	// XStream instances are not thread-safe.
    private ThreadLocal<XStream> _xstreams = new ThreadLocal<XStream>() {
        @Override protected XStream initialValue() {
            return new XStream();
        }
    };

    private XStream myXStream() {
        return _xstreams.get();
    }

    @Override
    public void serialize(OutputStream stream, Object object) throws IOException {
    	try {
    		BinaryStreamWriter writer = new BinaryStreamWriter(stream);
			myXStream().marshal(object, writer);
			writer.flush();
		} catch (RuntimeException rx) {
			throw new IOException(rx);
		}
    }

	@Override
	public byte[] serialize(Object object) {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		try {
			serialize(result, object);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return result.toByteArray(); 
	}

	@Override
	public Object deserialize(InputStream stream, ClassLoader classloader) throws IOException,	ClassNotFoundException {
		myXStream().setClassLoader(classloader);
		try {
			return myXStream().unmarshal(
				new BinaryStreamReader(stream));
		} catch (CannotResolveClassException e) {
			throw new ClassNotFoundException(e.getMessage());
		} catch (RuntimeException rx) {
			throw new IOException(rx.getMessage());
		}
	}

	@Override
	public Object deserialize(byte[] bytes, ClassLoader classloader) throws ClassNotFoundException, IOException {
		return deserialize(new ByteArrayInputStream(bytes), classloader);
	}
}