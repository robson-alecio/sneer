package sneer.pulp.serialization;

import java.io.IOException;

import sneer.brickness.Brick;

public interface Serializer extends Brick {

	byte[] serialize(Object object);
	Object deserialize(byte[] bytes, ClassLoader classloader) throws ClassNotFoundException, IOException;

}
