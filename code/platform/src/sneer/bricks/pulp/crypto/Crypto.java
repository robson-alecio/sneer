package sneer.bricks.pulp.crypto;

import java.io.File;
import java.io.IOException;

import sneer.foundation.brickness.Brick;

@Brick
public interface Crypto {

	/**
	 * SHA512 + Whirpool512 
	 */
	Sneer1024 digest(byte[] input);

	Sneer1024 digest(File file) throws IOException;	

	Digester newDigester();

	Sneer1024 unmarshallSneer1024(byte[] bytes);

	String toHexa(byte[] bytes);
}
