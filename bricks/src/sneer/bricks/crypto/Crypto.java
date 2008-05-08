package sneer.bricks.crypto;

import java.io.File;
import java.io.IOException;


public interface Crypto {

	/**
	 * SHA512 + Whirpool512 
	 */
	Sneer1024 sneer1024(byte[] input);

	Sneer1024 sneer1024(File file) throws IOException;	

	Digester sneer1024();
	
	Sneer1024 wrap(byte[] sneer1024Bytes);

}
