package sneer.pulp.dyndns.updater.impl;

import java.io.IOException;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Inject;
import sneer.pulp.dyndns.updater.BadAuthException;
import sneer.pulp.dyndns.updater.InvalidHostException;
import sneer.pulp.dyndns.updater.RedundantUpdateException;
import sneer.pulp.dyndns.updater.ServerErrorException;
import sneer.pulp.dyndns.updater.UnexpectedResponseException;
import sneer.pulp.dyndns.updater.Updater;
import sneer.pulp.dyndns.updater.UpdaterException;
import sneer.pulp.httpclient.HttpClient;
import wheel.io.Base64;
import wheel.lang.Pair;

public class UpdaterImpl implements Updater {
	
	public static void main(String[] args) throws Exception {
		final Container container = ContainerUtils.newContainer();
		final Updater client = container.produce(Updater.class);
		client.update("test.dyndns.org", "test", "test", "123.45.67.89");
	}

	@Inject
	private static HttpClient _client;

	@Override
	public void update(final String host, final String user, final String password, String newIp) throws UpdaterException, IOException {
		final String response = submitUpdateRequest(host, user, password, newIp).trim();
		if (handleGoodResponse(newIp, response))
			return;
		handleFailure(response);
	}

	private void handleFailure(final String response) throws RedundantUpdateException, BadAuthException,
			InvalidHostException, ServerErrorException, UnexpectedResponseException {
		if (response.equals("nochg"))
			throw new RedundantUpdateException();
		if (response.equals("badauth"))
			throw new BadAuthException();
		if (equalsAny(response, "nohost", "numhost", "notfqdn", "abuse"))
			throw new InvalidHostException(response);
		if (equalsAny(response, "dnserr", "911"))
			throw new ServerErrorException(response);
		throw new UnexpectedResponseException(response);
	}

	private boolean handleGoodResponse(String ip, String response) {
		if (response.equals("good"))
			return true;
		if (response.equals("good " + ip))
			return true;
		return false;
	}

	private <T> boolean equalsAny(T match, T... candidates) {
		for (T candidate : candidates)
			if (match.equals(candidate)) return true;
		return false;
	}

	private String submitUpdateRequest(final String host, final String user, final String password, String ip) throws IOException {
		return _client.get(
				"https://members.dyndns.org/nic/update?hostname=" + host + "&myip=" + ip + "&wildcard=NOCHG&mx=NOCHG&backmx=NOCHG",
				Pair.pair("User-Agent", "Sneer - DynDns Client - 0.1"),
				Pair.pair("Authorization", "Basic " + encode(user + ":" + password)));
	}

	private String encode(final String text) {
		return Base64.encode(text);
	}

}
