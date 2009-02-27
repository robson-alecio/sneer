package sneer.pulp.dyndns.checkip.impl;

import static sneer.brickness.Environments.my;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sneer.pulp.dyndns.checkip.CheckIp;
import sneer.pulp.httpclient.HttpClient;
import wheel.io.Logger;

class CheckIpImpl implements CheckIp {
	
	private final HttpClient _client = my(HttpClient.class);
	
	private static final Pattern _responsePattern = Pattern.compile("<body>Current IP Address: ((\\d|\\.)+)</body>");

	@Override
	public String check() throws IOException {
		final String responseBody = submitHttpRequest();
		String result = parse(responseBody);
		Logger.log("Own Ip Checked: {}", result);
		return result;
	}

	private String parse(String responseBody) throws IOException {
		final Matcher matcher = _responsePattern.matcher(responseBody);
		if (!matcher.find())
			throwBadResponse(responseBody);

		String result = matcher.group(1);
		if (result == null)
			throwBadResponse(responseBody);
		
		return result;
	}

	private void throwBadResponse(String responseBody) throws IOException {
		String message = "Unrecognized checkip response: " + responseBody;
		Logger.log(message);
		throw new IOException(message);
	}

	private String submitHttpRequest() throws IOException {
		return _client.get("http://checkip.dyndns.org/");
	}
	
}
