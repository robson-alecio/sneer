package sneer.bricks.serialization;

import java.io.NotSerializableException;

public interface Serializer {

	byte[] serialize(Object object) throws NotSerializableException;

	Object deserialize(byte[] packetReceived, ClassLoader classloader) throws ClassNotFoundException;

}
