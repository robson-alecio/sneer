package sneer.bricks.pulp.httpclient;

import java.io.IOException;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Pair;

@Brick
public interface HttpClient {

	String get(String url, Pair<String, String>... headers) throws IOException;

}
