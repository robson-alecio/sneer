package sneer.pulp.dyndns.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.ContainerUtils;
import sneer.pulp.dyndns.DynDns;
import sneer.pulp.dyndns.DynDnsException;
import sneer.pulp.httpclient.HttpClient;
import sneer.pulp.httpclient.HttpRequest;
import sneer.pulp.httpclient.HttpResponse;
import wheel.io.Base64;

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
			final HttpRequest request = context.mock(HttpRequest.class);
			one(client).newRequest("https://members.dyndns.org/nic/update?hostname=" + hostname + "&myip=" + ip + "&wildcard=NOCHG&mx=NOCHG&backmx=NOCHG");
				will(returnValue(request));
			one(request).setHeader(with("User-Agent"), with(wellFormedUserAgent()));
			one(request).setHeader("Authorization", "Basic " + encode(user + ":" + password));
			final HttpResponse response = context.mock(HttpResponse.class);
			one(request).submit(); will(returnValue(response));
			one(response).body(); will(returnValue(responseText));
		}});
		return client;
	}
	
	private BaseMatcher<String> wellFormedUserAgent() {
		return new BaseMatcher<String>() {
			@Override
			public boolean matches(Object value) {
				return 3 == StringUtils.split(value.toString(), '-').length;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("<well-formed user agent that includes company name, model number, and software build revision>");
			}
		};
	}

	public static String encode(String value) {
		return Base64.encode(value.getBytes());
	}
}
