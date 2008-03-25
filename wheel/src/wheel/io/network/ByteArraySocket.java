package wheel.io.network;

import java.io.Closeable;
import java.io.IOException;

public interface ByteArraySocket extends Closeable {

	byte[] read() throws IOException;

	void write(byte[] array) throws IOException;

}
