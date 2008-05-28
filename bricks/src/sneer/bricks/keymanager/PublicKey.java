package sneer.bricks.keymanager;

import java.io.Serializable;

public interface PublicKey extends Serializable {

	byte[] bytes();
	
	String toHexa();
}
