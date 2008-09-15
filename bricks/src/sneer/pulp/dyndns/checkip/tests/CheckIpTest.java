package sneer.pulp.dyndns.checkip.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.dyndns.checkip.CheckIp;
import sneer.pulp.httpclient.HttpClient;

public class CheckIpTest {
	
	private final Mockery _context = new JUnit4Mockery();
	
	@Test
	public void test() throws IOException {
		
		final String ip = "123.456.78.90";
		final HttpClient client = setUpHttpClientMockFor(ip);
		
		final Container container = ContainerUtils.newContainer(client);
		final CheckIp checkIp = container.produce(CheckIp.class);
		assertEquals(ip, checkIp.check());
		
		_context.assertIsSatisfied();
		
	}

	private HttpClient setUpHttpClientMockFor(final String ip) throws IOException {
		final String responseBody = 
			"<html><head><title>Current IP Check</title></head><body>Current IP Address: "
			+ ip
			+ "</body></html>";
		
		final HttpClient client = _context.mock(HttpClient.class);
		_context.checking(new Expectations() {{
			one(client).get("http://checkip.dyndns.org/"); will(returnValue(responseBody));
		}});
		return client;
	}

}
