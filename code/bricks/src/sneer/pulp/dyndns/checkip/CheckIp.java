package sneer.pulp.dyndns.checkip;

import java.io.IOException;

import sneer.brickness.Brick;

@Brick
public interface CheckIp {
	
	/** Never returns null. */
	String check() throws IOException;

}
