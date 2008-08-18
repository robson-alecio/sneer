package sneer.pulp.crypto;

import java.io.File;
import java.io.IOException;

import sneer.lego.Brick;


public interface Crypto extends Brick {

	/**
	 * SHA512 + Whirpool512 
	 */
	Sneer1024 digest(byte[] input);

	Sneer1024 digest(File file) throws IOException;	

	Digester digester();

	Sneer1024 unmarshallSneer1024(byte[] bytes);
}
