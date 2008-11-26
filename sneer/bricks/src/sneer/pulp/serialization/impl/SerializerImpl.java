package sneer.pulp.serialization.impl;

import java.io.IOException;

import sneer.pulp.serialization.Serializer;
import wheel.io.serialization.impl.XStreamBinarySerializer;

class SerializerImpl implements Serializer {

	final wheel.io.serialization.Serializer _serializer = new XStreamBinarySerializer();
	
	@Override
	public Object deserialize(byte[] bytes, ClassLoader classloader)
			throws ClassNotFoundException, IOException {
		return _serializer.deserialize(bytes, classloader);
		
	}

	@Override
	public byte[] serialize(Object object) {
		return _serializer.serialize(object);
	}
}