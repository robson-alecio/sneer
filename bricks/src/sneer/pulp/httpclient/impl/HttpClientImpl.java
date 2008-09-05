package sneer.pulp.httpclient.impl;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

import sneer.pulp.httpclient.HttpClient;
import wheel.lang.Pair;

class HttpClientImpl implements HttpClient {

	@Override
	public String get(String url, Pair<String, String>... headers) throws IOException {
		final URLConnection connection = new URL(url).openConnection();
		for (Pair<String, String> header : headers) {
			connection.setRequestProperty(header._a, header._b);
		}
		connection.connect();
		return IOUtils.toString(connection.getInputStream());
	}

}
