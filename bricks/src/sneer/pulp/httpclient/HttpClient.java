package sneer.pulp.httpclient;

import java.io.IOException;

import sneer.kernel.container.Brick;

public interface HttpClient extends Brick {

	HttpRequest newRequest(String url) throws IOException;

}
