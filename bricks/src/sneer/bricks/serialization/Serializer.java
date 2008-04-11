package sneer.bricks.serialization;

import java.io.NotSerializableException;

public interface Serializer {

	byte[] serialize(Object object) throws NotSerializableException;

}
