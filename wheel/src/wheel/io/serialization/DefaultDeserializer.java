package wheel.io.serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public class DefaultDeserializer {

	private final ClassLoader _classloader;
	
	public DefaultDeserializer(ClassLoader classloader) {
		_classloader = classloader;
	}
	
	public Object deserialize(byte[] bytes) throws ClassNotFoundException {
		try {
			ByteArrayInputStream inputBytes = new ByteArrayInputStream(bytes);
			ObjectInputStreamWithClassLoader inputStream = new ObjectInputStreamWithClassLoader(inputBytes, _classloader);
			return inputStream.readObject();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
