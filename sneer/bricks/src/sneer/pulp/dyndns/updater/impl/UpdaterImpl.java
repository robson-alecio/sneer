package sneer.pulp.dyndns.updater.impl;

import java.io.IOException;

import sneer.pulp.dyndns.updater.BadAuthException;
import sneer.pulp.dyndns.updater.InvalidHostException;
import sneer.pulp.dyndns.updater.RedundantUpdateException;
import sneer.pulp.dyndns.updater.ServerErrorException;
import sneer.pulp.dyndns.updater.UnexpectedResponseException;
import sneer.pulp.dyndns.updater.Updater;
import sneer.pulp.dyndns.updater.UpdaterException;
import sneer.pulp.httpclient.HttpClient;
import wheel.io.Base64;
import wheel.io.Logger;
import wheel.lang.Pair;
import static sneer.brickness.Environments.my;

class UpdaterImpl implements Updater {
	
	private final HttpClient _client = my(HttpClient.class);

	@Override
	public void update(final String host, final String user, final String password, String newIp) throws UpdaterException, IOException {
		final String response = submitUpdateRequest(host, user, password, newIp).trim();
		if (handleGoodResponse(newIp, response))
			return;
		handleFailure(response);
	}

	private void handleFailure(final String response) throws RedundantUpdateException, BadAuthException,
			InvalidHostException, ServerErrorException, UnexpectedResponseException {
		if (response.indexOf("nochg") != -1)
			throw new RedundantUpdateException();
		if (response.indexOf("badauth") != -1)
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

	private boolean equalsAny(String match, String... candidates) {
		for (String candidate : candidates)
			if (match.indexOf(candidate) != -1) return true;
		return false;
	}

	private String submitUpdateRequest(final String host, final String user, final String password, String ip) throws IOException {
		Logger.log("Submitting DynDns update for host: {} ip: {}", host, ip);
		return _client.get(
				"https://members.dyndns.org/nic/update?hostname=" + host + "&myip=" + ip + "&wildcard=NOCHG&mx=NOCHG&backmx=NOCHG",
				Pair.pair("User-Agent", "SneerAlfa - DynDns ClientAlfa - 0.1"),
				Pair.pair("Authorization", "Basic " + encode(user + ":" + password)));
	}

	private String encode(final String text) {
		return Base64.encode(text);
	}

}
