package sneer.bricks.crypto;

public interface Crypto {

	/**
	 * SHA512 + Whirpool512 
	 */
	byte[] sneer1024(byte[] input);	
}
