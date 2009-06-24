package sneer.bricks.pulp.dyndns.updater.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import sneer.bricks.hardware.io.codecs.base64.Base64;
import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.pulp.dyndns.updater.BadAuthException;
import sneer.bricks.pulp.dyndns.updater.InvalidHostException;
import sneer.bricks.pulp.dyndns.updater.RedundantUpdateException;
import sneer.bricks.pulp.dyndns.updater.ServerErrorException;
import sneer.bricks.pulp.dyndns.updater.UnexpectedResponseException;
import sneer.bricks.pulp.dyndns.updater.Updater;
import sneer.bricks.pulp.dyndns.updater.UpdaterException;
import sneer.bricks.pulp.httpclient.HttpClient;
import sneer.foundation.lang.Pair;

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
		my(Logger.class).log("Submitting DynDns update for host: {} ip: {}", host, ip);
		return _client.get(
				"https://members.dyndns.org/nic/update?hostname=" + host + "&myip=" + ip + "&wildcard=NOCHG&mx=NOCHG&backmx=NOCHG",
				Pair.of("User-Agent", "SneerAlfa - DynDns ClientAlfa - 0.1"),
				Pair.of("Authorization", "Basic " + encode(user + ":" + password)));
	}

	private String encode(final String text) {
		return my(Base64.class).encode(text);
	}

}
