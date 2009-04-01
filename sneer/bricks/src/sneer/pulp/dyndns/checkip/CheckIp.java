package sneer.pulp.dyndns.checkip;

import java.io.IOException;

import sneer.brickness.OldBrick;

public interface CheckIp extends OldBrick {
	
	/** Never returns null. */
	String check() throws IOException;

}
