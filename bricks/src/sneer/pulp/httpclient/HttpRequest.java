package sneer.pulp.httpclient;

import java.io.IOException;


public interface HttpRequest {

	void setHeader(String name, String value);

	HttpResponse submit() throws IOException;

}
