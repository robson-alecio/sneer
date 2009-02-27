package sneer.pulp.dyndns.checkip.tests;

import static sneer.brickness.Environments.my;

import java.io.IOException;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.pulp.dyndns.checkip.CheckIp;
import sneer.pulp.httpclient.HttpClient;
import tests.Contribute;
import tests.TestInContainerEnvironment;

public class CheckIpTest extends TestInContainerEnvironment {
	
	@Contribute final HttpClient _client = mock(HttpClient.class);
	
	@Test
	public void test() throws IOException {
		
		final String ip = "123.456.78.90";
		final String responseBody = 
			"<html><head><title>Current IP Check</title></head><body>Current IP Address: "
			+ ip
			+ "</body></html>";
		
		checking(new Expectations() {{
			one(_client).get("http://checkip.dyndns.org/"); will(returnValue(responseBody));
		}});
		
		final CheckIp checkIp = my(CheckIp.class);
		assertEquals(ip, checkIp.check());
		
	}
}
