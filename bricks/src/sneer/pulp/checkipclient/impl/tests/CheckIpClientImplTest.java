package sneer.pulp.checkipclient.impl.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.checkipclient.CheckIpClient;
import sneer.pulp.httpclient.HttpClient;
import sneer.pulp.httpclient.HttpRequest;
import sneer.pulp.httpclient.HttpResponse;

public class CheckIpClientImplTest {
	
	private final Mockery context = new Mockery();
	
	@Test
	public void test() throws IOException {
		
		final String ip = "123.456.78.90";
		final HttpClient client = setUpHttpClientMockFor(ip);
		
		final Container container = ContainerUtils.newContainer(client);
		final CheckIpClient checkIp = container.produce(CheckIpClient.class);
		assertEquals(ip, checkIp.check());
		
		context.assertIsSatisfied();
		
	}

	private HttpClient setUpHttpClientMockFor(final String ip) {
		final String responseBody = /*"HTTP/1.1 200 OK\n" +
			"Content-Type: text/html\n" +
			"Server: DynDNS-CheckIP/1.0\n" +
			"Cache-Control: no-cache\n" +
			"Pragma: no-cache\n" +
			"Content-Length: 105\n" +
			"Connection: close\n" +
			"\n" +*/
			"<html><head><title>Current IP Check</title></head><body>Current IP Address: "
			+ ip
			+ "</body></html>";
		
		final HttpClient client = context.mock(HttpClient.class);
		context.checking(new Expectations() {{
			final HttpRequest request = context.mock(HttpRequest.class);
			one(client).newRequest("http://checkip.dyndns.org/"); will(returnValue(request));
			HttpResponse response = context.mock(HttpResponse.class);
			one(response).body(); will(returnValue(responseBody));
			one(request).submit(); will(returnValue(response));
		}});
		return client;
	}

}
