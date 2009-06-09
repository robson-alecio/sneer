package sneer.pulp.crypto;

import java.io.Serializable;

public interface Sneer1024 extends Serializable {

	byte[] bytes();
	
	String toHexa();

}
