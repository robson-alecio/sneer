package sneer.pulp.httpclient.impl;

import java.io.IOException;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

import sneer.pulp.httpclient.HttpResponse;

class HttpResponseImpl implements HttpResponse {

	private final URLConnection _connection;

	public HttpResponseImpl(URLConnection connection) {
		_connection = connection;
	}

	@Override
	public String body() throws IOException {
		return IOUtils.toString(_connection.getInputStream());
	}

}
