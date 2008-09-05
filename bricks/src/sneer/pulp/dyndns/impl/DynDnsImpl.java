package sneer.pulp.dyndns.impl;

import java.io.IOException;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Inject;
import sneer.pulp.dyndns.DynDns;
import sneer.pulp.dyndns.DynDnsException;
import sneer.pulp.httpclient.HttpClient;
import sneer.pulp.httpclient.HttpRequest;
import sneer.pulp.httpclient.HttpResponse;
import wheel.io.Base64;

public class DynDnsImpl implements DynDns {
	
	public static void main(String[] args) throws Exception {
		final Container container = ContainerUtils.newContainer();
		final DynDns client = container.produce(DynDns.class);
		if (client.update("test.dyndns.org", "123.45.67.89", "test", "test")) {
			System.out.println("record updated");
		}
	}

	@Inject
	private static HttpClient _client;
	
	@Override
	public boolean update(String hostname, String ip, String user, String password) throws DynDnsException {
		final String response = submitUpdateRequest(hostname, ip, user, password).trim();
		if (response.startsWith("good"))
			return true;
		if (response.startsWith("nochg"))
			return false;
		throw new DynDnsException(response);
	}

	private String submitUpdateRequest(String hostname, String ip, String user, String password) {
		try {
			final HttpRequest request = _client.newRequest("https://members.dyndns.org/nic/update?hostname=" + hostname + "&myip=" + ip + "&wildcard=NOCHG&mx=NOCHG&backmx=NOCHG");
			request.setHeader("User-Agent", "Sneer - DynDns Client - 0.1");
			request.setHeader("Authorization", "Basic " + encode(user + ":" + password));
			final HttpResponse response = request.submit();
			return response.body();
		} catch (IOException e) {
			throw new DynDnsException(e);
		}
	}

	private String encode(final String text) {
		return Base64.encode(text.getBytes());
	}

}
