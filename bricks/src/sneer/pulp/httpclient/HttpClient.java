package sneer.pulp.httpclient;

import sneer.kernel.container.Brick;

public interface HttpClient extends Brick {

	HttpRequest newRequest(String url);

}
