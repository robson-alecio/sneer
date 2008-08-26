package wheel.io.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A strategy for writing objects to and reading objects from streams. Implementations <b>must</b> be safe for
 * concurrent use by multiple threads.
 * <p>
 * If an implementation will be used for snapshots, it must be able to write and read the prevalent system it will
 * be used with, but does not need to be able to write or read any other objects. If an implementation will be used
 * for journals, it must be able to write and read any transactions it will be used with, but does not need to be
 * able to write or read any other objects.
 */
public interface Serializer {

	Object deserialize(byte[] bytes, ClassLoader classloader) throws ClassNotFoundException, IOException;
	byte[] serialize(Object object);


	/**
	 * Writes an object to a stream. An implementation can expect that the
	 * stream is already buffered, so additional buffering is not required for performance.
	 */
	public void serialize(OutputStream stream, Object object) throws IOException;

	/**
	 * Reads an object from a stream. An implementation can expect that the
	 * stream is already buffered, so additional buffering is not required for performance.
	 */
	Object deserialize(InputStream stream, ClassLoader classloader) throws IOException, ClassNotFoundException;


}
