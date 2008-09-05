package sneer.pulp.httpclient.impl;

import java.io.IOException;
import java.net.URLConnection;

import sneer.pulp.httpclient.HttpRequest;
import sneer.pulp.httpclient.HttpResponse;

public class HttpRequestImpl implements HttpRequest {

	private final URLConnection _connection;

	public HttpRequestImpl(URLConnection openConnection) {
		_connection = openConnection;
	}

	@Override
	public void setHeader(String name, String value) {
		_connection.setRequestProperty(name, value);
	}

	@Override
	public HttpResponse submit() throws IOException {
		_connection.connect();
		return new HttpResponseImpl(_connection); 
	}

}
