package sneer.pulp.dyndns.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.ContainerUtils;
import sneer.pulp.dyndns.DynDns;
import sneer.pulp.dyndns.DynDnsException;
import sneer.pulp.httpclient.HttpClient;
import wheel.io.Base64;
import wheel.lang.Pair;
import wheel.lang.Types;

public class DynDnsTest {

	private final Mockery context = new JUnit4Mockery();
	
	@Test
	public void testUpdateSuccess() throws IOException {
		assertTrue(runDynDnsWithResponse("good 123.456.78.90\n"));
	}
	
	@Test
	public void testUpdateNoChange() throws IOException {
		assertFalse(runDynDnsWithResponse("nochg\n"));
	}
	
	@Test(expected=DynDnsException.class)
	public void testFailure() throws IOException {
		runDynDnsWithResponse("abuse\n");
	}

	private boolean runDynDnsWithResponse(final String responseText) throws IOException {
		final String hostname = "hostname";
		final String ip = "123.456.78.90";
		final String user = "user";
		final String password = "password";
		
		final HttpClient client = setUpHttpClientMockFor(hostname, ip, user, password, responseText);
		
		final DynDns dyndns = ContainerUtils.newContainer(client).produce(DynDns.class);
		final boolean returnValue = dyndns.update(hostname, ip, user, password);
		
		context.assertIsSatisfied();
		
		return returnValue;
	}

	private HttpClient setUpHttpClientMockFor(final String hostname,
			final String ip, final String user, final String password,
			final String responseText) throws IOException {
		
		final HttpClient client = context.mock(HttpClient.class);
		context.checking(new Expectations() {{
			one(client).get(
				with("https://members.dyndns.org/nic/update?hostname=" + hostname + "&myip=" + ip + "&wildcard=NOCHG&mx=NOCHG&backmx=NOCHG"),
				with(pairs(
						Pair.pair("User-Agent", "Sneer - DynDns Client - 0.1"),
						Pair.pair("Authorization", "Basic " + encode(user + ":" + password)))));
			will(returnValue(responseText));
		}});
		return client;
	}
	
	private <A, B> Matcher<Pair<A, B>[]> pairs(final Pair<A, B>...expected) {
		return new BaseMatcher<Pair<A, B>[]>() {
			@Override
			public boolean matches(Object value) {
				final Pair<A, B>[] actual = Types.cast(value);
				if (actual.length != expected.length)
					return false;
				
				for (int i = 0; i < expected.length; i++) {
					if (!actual[i].equals(expected[i])) {
						return false;
					}
				}
				
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendValueList("[", ", ", "]", expected);
			}
		};
	}

	public static String encode(String value) {
		return Base64.encode(value);
	}
}
