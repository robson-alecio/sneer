package sneer.pulp.dyndns.ownip.tests;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.clock.Clock;
import sneer.pulp.dyndns.checkip.CheckIp;
import sneer.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.pulp.propertystore.PropertyStore;
import sneer.pulp.propertystore.mocks.TransientPropertyStore;
import wheel.lang.Omnivore;
import wheel.reactive.impl.Receiver;

public class OwnIpDiscovererTest {
	
	final Mockery context = new JUnit4Mockery();
	
	@Test
	public void testFirstTime() throws IOException {
		
		final CheckIp checkip = context.mock(CheckIp.class);
		final Omnivore<String> receiver = context.mock(Omnivore.class);
		final PropertyStore store = new TransientPropertyStore();
		
		final String ip1 = "123.45.67.89";
		final String ip2 = "12.34.56.78";

		int retryTime = 11 * 60 * 1000;

		context.checking(new Expectations() {{
			final Sequence seq = context.sequence("sequence");
			
			one(checkip).check(); will(returnValue(ip1)); inSequence(seq);
			one(receiver).consume(ip1); inSequence(seq);
			
			one(checkip).check(); will(returnValue(ip1)); inSequence(seq);
			
			one(checkip).check(); will(returnValue(ip2)); inSequence(seq);
			one(receiver).consume(ip2); inSequence(seq);
			
		}});
		
		final Container container = ContainerUtils.newContainer(checkip, store);
		final OwnIpDiscoverer discoverer = container.produce(OwnIpDiscoverer.class);
		
		@SuppressWarnings("unused")
		final Receiver<String> refToAvoidGc = new Receiver<String>(discoverer.ownIp()) { @Override public void consume(String value) {
			receiver.consume(value);
		}};
		
		Clock clock = container.produce(Clock.class);
		clock.advanceTime(1);
		clock.advanceTime(retryTime * 3);
		
		context.assertIsSatisfied();
	}

}
