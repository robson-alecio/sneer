package sneer.pulp.httpclient.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

import sneer.commons.lang.Pair;
import sneer.pulp.httpclient.HttpClient;

class HttpClientImpl implements HttpClient {

	@Override
	public String get(String url, Pair<String, String>... headers) throws IOException {
		final URLConnection connection = new URL(url).openConnection();
		setRequestProperties(connection, headers);
		return readString(connection);
	}

	private void setRequestProperties(final URLConnection connection, Pair<String, String>... headers) {
		for (Pair<String, String> header : headers)
			connection.setRequestProperty(header._a, header._b);
	}

	private String readString(final URLConnection connection) throws IOException {
		final InputStream is = connection.getInputStream();
		try {
			return IOUtils.toString(is);
		} finally {
			is.close();
		}
	}
}
