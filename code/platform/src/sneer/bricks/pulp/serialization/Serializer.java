package sneer.bricks.pulp.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sneer.foundation.brickness.Brick;

/**
 * A strategy for writing objects to and reading objects from streams. Implementations <b>must</b> be safe for
 * concurrent use by multiple threads.
 * <p>
 * If an implementation will be used for snapshots, it must be able to write and read the prevalent system it will
 * be used with, but does not need to be able to write or read any other objects. If an implementation will be used
 * for journals, it must be able to write and read any transactions it will be used with, but does not need to be
 * able to write or read any other objects.
 */
@Brick
public interface Serializer {

	/**
	 * Writes an object to a stream. An implementation can expect that the
	 * stream is already buffered, so additional buffering is not required for performance.
	 */
	void serialize(OutputStream stream, Object object) throws IOException;

	byte[] serialize(Object object);

	/**
	 * Reads an object from a stream. An implementation can expect that the
	 * stream is already buffered, so additional buffering is not required for performance.
	 */
	Object deserialize(InputStream stream, ClassLoader classloader) throws IOException, ClassNotFoundException;

	Object deserialize(byte[] bytes, ClassLoader classloader) throws ClassNotFoundException, IOException;
}