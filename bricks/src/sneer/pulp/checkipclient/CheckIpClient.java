package sneer.pulp.checkipclient;

import java.io.IOException;

import sneer.kernel.container.Brick;

public interface CheckIpClient extends Brick {
	
	String check() throws IOException;

}
