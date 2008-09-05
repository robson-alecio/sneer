package sneer.pulp.dyndns.ownip.tests;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import sneer.kernel.container.ContainerUtils;
import sneer.pulp.clock.mocks.ClockMock;
import sneer.pulp.dyndns.checkip.CheckIp;
import sneer.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.pulp.propertystore.PropertyStore;
import wheel.lang.Omnivore;
import wheel.reactive.impl.Receiver;

public class OwnIpDiscovererTest {
	
	private final Mockery context = new JUnit4Mockery();
	
	@Test
	public void testFirstTime() throws IOException {
		
		final ClockMock clock = new ClockMock();
		final CheckIp checkip = context.mock(CheckIp.class);
		final PropertyStore store = context.mock(PropertyStore.class);
		final Omnivore<String> ownIp = context.mock(Omnivore.class);
		
		final String ip1 = "123.45.67.89";
		final String ip2 = "12.34.56.78";
		
		context.checking(new Expectations() {{
			final Sequence seq = context.sequence("sequence");
			
			one(checkip).check(); will(returnValue(ip1)); inSequence(seq);
			one(store).get("ownIp"); will(returnValue(null)); inSequence(seq);
			one(store).set("ownIp", ip1); inSequence(seq);
			one(ownIp).consume(ip1); inSequence(seq);
			
			one(checkip).check(); will(returnValue(ip1)); inSequence(seq);
			one(store).get("ownIp"); will(returnValue(ip1)); inSequence(seq);
			
			one(checkip).check(); will(returnValue(ip2)); inSequence(seq);
			one(store).get("ownIp"); will(returnValue(ip1)); inSequence(seq);
			one(store).set("ownIp", ip2); inSequence(seq);
			one(ownIp).consume(ip2); inSequence(seq);
			
		}});
		
		final OwnIpDiscoverer discoverer = ContainerUtils.newContainer(clock, checkip, store).produce(OwnIpDiscoverer.class);
		
		@SuppressWarnings("unused")
		final Receiver<String> receiver = new Receiver<String>(discoverer.ownIp()) { @Override public void consume(String value) {
			ownIp.consume(value);
		}};
		
		clock.triggerAlarm(0);
		clock.triggerAlarm(0);
		clock.triggerAlarm(0);
		
		context.assertIsSatisfied();
	}

}
