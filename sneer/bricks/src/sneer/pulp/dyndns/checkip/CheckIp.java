package sneer.pulp.dyndns.checkip;

import java.io.IOException;

import sneer.kernel.container.Brick;

public interface CheckIp extends Brick {
	
	String check() throws IOException;

}
