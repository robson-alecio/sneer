package sneer.pulp.httpclient;

import java.io.IOException;

import sneer.brickness.OldBrick;
import sneer.commons.lang.Pair;

public interface HttpClient extends OldBrick {

	String get(String url, Pair<String, String>... headers) throws IOException;

}
