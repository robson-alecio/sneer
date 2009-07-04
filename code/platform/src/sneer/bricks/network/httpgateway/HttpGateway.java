package sneer.bricks.network.httpgateway;

import java.io.IOException;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface HttpGateway {

	void get(final String httpUrl, final Consumer<byte[]> response, final Consumer<IOException> exception);
	
}
