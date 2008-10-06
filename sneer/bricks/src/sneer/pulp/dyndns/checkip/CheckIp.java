package sneer.pulp.dyndns.checkip;

import java.io.IOException;

import sneer.kernel.container.Brick;

public interface CheckIp extends Brick {
	
	/** Never returns null. */
	String check() throws IOException;

}
