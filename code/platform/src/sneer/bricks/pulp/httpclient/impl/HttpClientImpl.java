package sneer.bricks.pulp.httpclient.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import sneer.bricks.hardware.io.IO;
import sneer.bricks.pulp.httpclient.HttpClient;
import sneer.foundation.lang.Pair;

class HttpClientImpl implements HttpClient {

	@Override
	public String get(String url, Pair<String, String>... headers) throws IOException {
		final URLConnection connection = new URL(url).openConnection();
		setRequestProperties(connection, headers);
		return readString(connection);
	}

	private void setRequestProperties(final URLConnection connection, Pair<String, String>... headers) {
		for (Pair<String, String> header : headers)
			connection.setRequestProperty(header.a, header.b);
	}

	private String readString(final URLConnection connection) throws IOException {
		final InputStream is = connection.getInputStream();
		try {
			return my(IO.class).streams().toString(is);
		} finally {
			try { is.close(); } catch (Throwable ignore) { }
		}
	}
}
