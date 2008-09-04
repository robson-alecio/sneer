package sneer.pulp.checkipclient.impl;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Inject;
import sneer.pulp.checkipclient.CheckIpClient;
import sneer.pulp.httpclient.HttpClient;
import sneer.pulp.httpclient.HttpRequest;

public class CheckIpClientImpl implements CheckIpClient {
	
	public static void main(String[] args) throws Exception {
		final Container container = ContainerUtils.newContainer();
		final CheckIpClient client = container.produce(CheckIpClient.class);
		System.out.println(client.check());
	}
	
	@Inject
	private static HttpClient _client;
	
	private static final Pattern _responsePattern = Pattern.compile("<body>Current IP Address: ((\\d|\\.)+)</body>");

	@Override
	public String check() throws IOException {
		final String responseBody = submitHttpRequest();
		return parse(responseBody);
	}

	private String parse(final String responseBody) {
		final Matcher matcher = _responsePattern.matcher(responseBody);
		if (!matcher.find()) {
			throw new IllegalStateException("Unrecognized checkip response: " + responseBody);
		}
		return matcher.group(1);
	}

	private String submitHttpRequest() throws IOException {
		final HttpRequest request = _client.newRequest("http://checkip.dyndns.org/");
		return request.submit().body();
	}
}
