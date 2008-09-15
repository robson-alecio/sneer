package sneer.pulp.dyndns.checkip.impl;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Inject;
import sneer.pulp.dyndns.checkip.CheckIp;
import sneer.pulp.httpclient.HttpClient;

class CheckIpImpl implements CheckIp {
	
	@Inject
	private static HttpClient _client;
	
	private static final Pattern _responsePattern = Pattern.compile("<body>Current IP Address: ((\\d|\\.)+)</body>");

	@Override
	public String check() throws IOException {
		final String responseBody = submitHttpRequest();
		return parse(responseBody);
	}

	private String parse(final String responseBody) throws IOException {
		final Matcher matcher = _responsePattern.matcher(responseBody);
		if (!matcher.find()) {
			throw new IOException("Unrecognized checkip response: " + responseBody);
		}
		return matcher.group(1);
	}

	private String submitHttpRequest() throws IOException {
		return _client.get("http://checkip.dyndns.org/");
	}
	
	public static void main(String[] args) throws Exception {
		final Container container = ContainerUtils.newContainer();
		final CheckIp client = container.produce(CheckIp.class);
		System.out.println(client.check());
	}
}
