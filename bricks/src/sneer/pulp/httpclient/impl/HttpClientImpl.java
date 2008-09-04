package sneer.pulp.httpclient.impl;

import java.io.IOException;
import java.net.URL;

import sneer.pulp.httpclient.HttpClient;
import sneer.pulp.httpclient.HttpRequest;

class HttpClientImpl implements HttpClient {

	@Override
	public HttpRequest newRequest(String url) throws IOException {
		return new HttpRequestImpl(new URL(url).openConnection());
	}

}
