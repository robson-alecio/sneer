package sneer.pulp.serialization;

import java.io.IOException;

import sneer.brickness.OldBrick;

public interface Serializer extends OldBrick {

	byte[] serialize(Object object);
	Object deserialize(byte[] bytes, ClassLoader classloader) throws ClassNotFoundException, IOException;

}
