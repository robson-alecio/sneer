package sneer.bricks.network.httpgateway;

import java.io.IOException;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface HttpGateway {

	void get(String httpUrl, Consumer<byte[]> response);
	void get(String httpUrl, Consumer<byte[]> response, Consumer<IOException> exception);
	
}
