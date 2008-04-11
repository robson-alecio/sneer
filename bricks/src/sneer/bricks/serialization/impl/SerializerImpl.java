package sneer.bricks.serialization.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;

import sneer.bricks.serialization.Serializer;
import wheel.io.Streams;

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

}
