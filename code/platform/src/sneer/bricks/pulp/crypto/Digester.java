package sneer.bricks.pulp.crypto;

import java.io.IOException;
import java.io.InputStream;

public interface Digester {

	void update(InputStream is) throws IOException;

	byte[] digest();

}
