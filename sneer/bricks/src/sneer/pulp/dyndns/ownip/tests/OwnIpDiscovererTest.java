package sneer.pulp.dyndns.ownip.tests;

import static wheel.lang.Environments.my;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.pulp.clock.Clock;
import sneer.pulp.dyndns.checkip.CheckIp;
import sneer.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.pulp.propertystore.PropertyStore;
import sneer.pulp.propertystore.mocks.TransientPropertyStore;
import tests.JMockContainerEnvironment;
import wheel.lang.Consumer;
import wheel.reactive.impl.Receiver;

@RunWith(JMockContainerEnvironment.class)
public class OwnIpDiscovererTest {
	
	final Mockery _context = new JUnit4Mockery();
	final CheckIp checkip = _context.mock(CheckIp.class);
	final PropertyStore store = new TransientPropertyStore();
	
	@Test
	public void testDiscovery() throws IOException {
		final Consumer<String> receiver = _context.mock(Consumer.class);
		final String ip1 = "123.45.67.89";
		final String ip2 = "12.34.56.78";

		int retryTime = 11 * 60 * 1000;

		_context.checking(new Expectations() {{
			final Sequence seq = _context.sequence("sequence");

			one(checkip).check(); will(returnValue(ip1)); inSequence(seq);
			one(receiver).consume(ip1); inSequence(seq);
			
			one(checkip).check(); will(returnValue(ip1)); inSequence(seq);
			
			one(checkip).check(); will(returnValue(ip2)); inSequence(seq);
			one(receiver).consume(ip2); inSequence(seq);
			
			one(checkip).check(); will(returnValue(ip2)); inSequence(seq);

		}});
		
		OwnIpDiscoverer discoverer = my(OwnIpDiscoverer.class);
		
		@SuppressWarnings("unused")
		final Receiver<String> refToAvoidGc = new Receiver<String>(discoverer.ownIp()) { @Override public void consume(String value) {
			receiver.consume(value);
		}};
		
		Clock clock = my(Clock.class);
		clock.advanceTime(retryTime);
		clock.advanceTime(retryTime);
		clock.advanceTime(retryTime);
	}
}