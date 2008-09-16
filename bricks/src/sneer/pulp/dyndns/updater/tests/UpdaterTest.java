package sneer.pulp.dyndns.updater.tests;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

import sneer.kernel.container.ContainerUtils;
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

public class UpdaterTest {

	private Mockery _context;
	
	@Before
	public void beforeUpdaterTest() {
		_context = new JUnit4Mockery();
	}
	
	@Test
	public void testUpdateSuccess() throws Exception {
		runUpdaterWithResponse("good 123.456.78.90\n");
	}

	@Test(expected=UnexpectedResponseException.class)
	public void testUpdateSuccessNot() throws Exception {
		runUpdaterWithResponse("good 127.0.0.1\n");
	}
	
	@Test(expected=ServerErrorException.class)
	public void testDnsError() throws Exception {
		runUpdaterWithResponse("dnserr\n");
	}
	
	@Test(expected=ServerErrorException.class)
	public void test911() throws Exception {
		runUpdaterWithResponse("911\n");
	}
	
	@Test(expected=UnexpectedResponseException.class)
	public void testUnexpectedResponse() throws Exception {
		runUpdaterWithResponse("sbrobbles");
	}
	
	@Test(expected=BadAuthException.class)
	public void testBadAuth() throws Exception {
		runUpdaterWithResponse("badauth\n");
	}
	
	@Test(expected=RedundantUpdateException.class)
	public void testNoChange() throws Exception {
		runUpdaterWithResponse("nochg\n");
	}
	
	@Test(expected=InvalidHostException.class)
	public void testAbuse() throws Exception {
		runUpdaterWithResponse("abuse\n");
	}
	
	@Test(expected=InvalidHostException.class)
	public void testNoHost() throws Exception {
		runUpdaterWithResponse("nohost\n");
	}
	
	@Test(expected=InvalidHostException.class)
	public void testNumHost() throws Exception {
		runUpdaterWithResponse("numhost\n");
	}
	
	@Test(expected=InvalidHostException.class)
	public void testNotFullyQualifiedDomainName() throws Exception {
		runUpdaterWithResponse("notfqdn\n");
	}

	private void runUpdaterWithResponse(final String responseText) throws IOException, UpdaterException {
		final String hostname = "hostname";
		final String ip = "123.456.78.90";
		final String user = "user";
		final String password = "password";
		
		final HttpClient client = setUpHttpClientMockFor(hostname, ip, user, password, responseText);
		
		final Updater updater = ContainerUtils.newContainer(client).produce(Updater.class);
		updater.update(hostname, user, password, ip);
		
		_context.assertIsSatisfied();
		
	}

	private HttpClient setUpHttpClientMockFor(final String hostname,
			final String ip, final String user, final String password,
			final String responseText) throws IOException {
		
		final HttpClient client = _context.mock(HttpClient.class);
		_context.checking(new Expectations() {{
			one(client).get(
				"https://members.dyndns.org/nic/update?hostname=" + hostname + "&myip=" + ip + "&wildcard=NOCHG&mx=NOCHG&backmx=NOCHG",
				Pair.pair("User-Agent", "SneerAlfa - DynDns ClientAlfa - 0.1"),
				Pair.pair("Authorization", "Basic " + encode(user + ":" + password)));
			will(returnValue(responseText));
		}});
		return client;
	}
	
	public static String encode(String value) {
		return Base64.encode(value);
	}
}
