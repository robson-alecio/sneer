package sneer.bricks.network;

import java.io.IOException;

import sneer.lego.Crashable;

public interface ByteArraySocket extends Crashable {

	byte[] read() throws IOException;

	void write(byte[] array) throws IOException;

}
