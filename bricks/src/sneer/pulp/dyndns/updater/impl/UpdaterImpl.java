package sneer.pulp.dyndns.updater.impl;

import java.io.IOException;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.Inject;
import sneer.pulp.dyndns.ownaccount.Account;
import sneer.pulp.dyndns.ownaccount.impl.SimpleAccount;
import sneer.pulp.dyndns.updater.Updater;
import sneer.pulp.dyndns.updater.UpdaterException;
import sneer.pulp.httpclient.HttpClient;
import wheel.io.Base64;
import wheel.lang.Pair;

public class UpdaterImpl implements Updater {
	
	public static void main(String[] args) throws Exception {
		final Container container = ContainerUtils.newContainer();
		final Updater client = container.produce(Updater.class);
		if (client.update(new SimpleAccount("test.dyndns.org", "test", "test"), "123.45.67.89")) {
			System.out.println("record updated");
		}
	}

	@Inject
	private static HttpClient _client;
	
	@Override
	public boolean update(Account account, String ip) throws UpdaterException {
		final String response = submitUpdateRequest(account, ip).trim();
		if (response.startsWith("good"))
			return true;
		if (response.startsWith("nochg"))
			return false;
		throw new UpdaterException(response);
	}

	private String submitUpdateRequest(Account account, String ip) {
		try {
			final String response = _client.get(
					"https://members.dyndns.org/nic/update?hostname=" + account.host() + "&myip=" + ip + "&wildcard=NOCHG&mx=NOCHG&backmx=NOCHG",
					Pair.pair("User-Agent", "Sneer - DynDns Client - 0.1"),
					Pair.pair("Authorization", "Basic " + encode(account.user() + ":" + account.password())));
			return response;
		} catch (IOException e) {
			throw new UpdaterException(e);
		}
	}

	private String encode(final String text) {
		return Base64.encode(text);
	}

}
