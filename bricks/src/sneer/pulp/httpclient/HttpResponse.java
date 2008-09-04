package sneer.pulp.httpclient;

import java.io.IOException;

public interface HttpResponse {

	String body() throws IOException;

}
