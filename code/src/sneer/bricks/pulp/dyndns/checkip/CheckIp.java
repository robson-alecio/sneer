package sneer.bricks.pulp.dyndns.checkip;

import java.io.IOException;

import sneer.foundation.brickness.Brick;

@Brick
public interface CheckIp {
	
	/** Never returns null. */
	String check() throws IOException;

}
