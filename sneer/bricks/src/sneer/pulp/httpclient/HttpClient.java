package sneer.pulp.httpclient;

import java.io.IOException;

import sneer.brickness.Brick;
import sneer.commons.lang.Pair;

@Brick
public interface HttpClient {

	String get(String url, Pair<String, String>... headers) throws IOException;

}
