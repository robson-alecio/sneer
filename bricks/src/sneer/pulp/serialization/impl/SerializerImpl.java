package sneer.pulp.serialization.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;

import sneer.pulp.serialization.Serializer;
import wheel.io.Streams;
import wheel.io.serialization.ObjectInputStreamWithClassLoader;

public class SerializerImpl implements Serializer {

	public byte[] serialize(Object object) throws NotSerializableException {
		ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
		ObjectOutputStream outputStream = null;
		try {

			outputStream = new ObjectOutputStream(outputBytes);
			outputStream.writeObject(object);
			return outputBytes.toByteArray();
			
		} catch (NotSerializableException nse) {
			throw nse;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (outputStream != null)
				Streams.crash(outputStream);
		}
	}

	@Override
	public Object deserialize(byte[] bytes, ClassLoader classloader) throws ClassNotFoundException {
		try {
			ByteArrayInputStream inputBytes = new ByteArrayInputStream(bytes);
			ObjectInputStreamWithClassLoader inputStream = new ObjectInputStreamWithClassLoader(inputBytes, classloader);
			return inputStream.readObject();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
