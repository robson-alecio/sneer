package sneer.bricks.serialization.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;

import sneer.bricks.serialization.Serializer;

public class SerializerImpl implements Serializer {

	public byte[] serialize(Object object) throws NotSerializableException {
		//Optimize Consider using the OptimizedSerializer once Sneer is no longer dropping packets.

		ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(outputBytes);
			outputStream.writeObject(object);
			outputStream.close();
			
			return outputBytes.toByteArray();
			
		} catch (NotSerializableException nse) {
			throw nse;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
