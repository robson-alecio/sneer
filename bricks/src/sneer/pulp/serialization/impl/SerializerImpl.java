package sneer.pulp.serialization.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import sneer.pulp.serialization.Serializer;
import sneer.pulp.serialization.mocks.XStreamBinarySerializer;

public class SerializerImpl extends XStreamBinarySerializer implements Serializer {

	public byte[] serialize(Object object){
		ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
		writeObject(outputBytes, object);
		return outputBytes.toByteArray(); 
	}

	@Override
	public Object deserialize(byte[] bytes, ClassLoader classloader){
		getXStream().setClassLoader(classloader);
		ByteArrayInputStream inputBytes = new ByteArrayInputStream(bytes);
		return readObject(inputBytes);
	}

}
