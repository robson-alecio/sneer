package sneer.pulp.network;

import java.io.IOException;

import sneer.kernel.container.Crashable;

public interface ByteArraySocket extends Crashable {

	byte[] read() throws IOException;

	void write(byte[] array) throws IOException;

}
