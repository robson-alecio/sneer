package sneer.bricks.pulp.dyndns.updater.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.bricks.hardware.io.codecs.base64.Base64;
import sneer.bricks.pulp.dyndns.updater.BadAuthException;
import sneer.bricks.pulp.dyndns.updater.InvalidHostException;
import sneer.bricks.pulp.dyndns.updater.RedundantUpdateException;
import sneer.bricks.pulp.dyndns.updater.ServerErrorException;
import sneer.bricks.pulp.dyndns.updater.UnexpectedResponseException;
import sneer.bricks.pulp.dyndns.updater.Updater;
import sneer.bricks.pulp.dyndns.updater.UpdaterException;
import sneer.bricks.pulp.httpclient.HttpClient;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.Bind;
import sneer.foundation.lang.Pair;

public class UpdaterTest extends BrickTest {

	@Bind final HttpClient client = mock(HttpClient.class);
	
	@Test
	public void testUpdateSuccess() throws Exception {
		setUpExpectations("good 123.456.78.90\n");
	}

	@Test(expected=UnexpectedResponseException.class)
	public void testUpdateSuccessNot() throws Exception {
		setUpExpectations("good 127.0.0.1\n");
	}
	
	@Test(expected=ServerErrorException.class)
	public void testDnsError() throws Exception {
		setUpExpectations("dnserr\n");
	}
	
	@Test(expected=ServerErrorException.class)
	public void test911() throws Exception {
		setUpExpectations("911\n");
	}
	
	@Test(expected=UnexpectedResponseException.class)
	public void testUnexpectedResponse() throws Exception {
		setUpExpectations("sbrobbles");
	}
	
	@Test(expected=BadAuthException.class)
	public void testBadAuth() throws Exception {
		setUpExpectations("badauth\n");
	}
	
	@Test(expected=RedundantUpdateException.class)
	public void testNoChange() throws Exception {
		setUpExpectations("nochg\n");
	}
	
	@Test(expected=InvalidHostException.class)
	public void testAbuse() throws Exception {
		setUpExpectations("abuse\n");
	}
	
	@Test(expected=InvalidHostException.class)
	public void testNoHost() throws Exception {
		setUpExpectations("nohost\n");
	}
	
	@Test(expected=InvalidHostException.class)
	public void testNumHost() throws Exception {
		setUpExpectations("numhost\n");
	}
	
	@Test(expected=InvalidHostException.class)
	public void testNotFullyQualifiedDomainName() throws Exception {
		setUpExpectations("notfqdn\n");
	}

	private void setUpExpectations(final String responseText) throws IOException, UpdaterException {
		final String hostname = "hostname";
		final String ip = "123.456.78.90";
		final String user = "user";
		final String password = "password";
		
		setUpHttpClientMockFor(hostname, ip, user, password, responseText);
		
		final Updater updater = my(Updater.class);
		updater.update(hostname, user, password, ip);
	}

	private void setUpHttpClientMockFor(final String hostname,
			final String ip, final String user, final String password,
			final String responseText) throws IOException {
		
		checking(new Expectations() {{
			one(client).get(
				"https://members.dyndns.org/nic/update?hostname=" + hostname + "&myip=" + ip + "&wildcard=NOCHG&mx=NOCHG&backmx=NOCHG",
				Pair.of("User-Agent", "SneerAlfa - DynDns ClientAlfa - 0.1"),
				Pair.of("Authorization", "Basic " + encode(user + ":" + password)));
			will(returnValue(responseText));
		}});
	}
	
	public static String encode(String value) {
		return my(Base64.class).encode(value);
	}
}
