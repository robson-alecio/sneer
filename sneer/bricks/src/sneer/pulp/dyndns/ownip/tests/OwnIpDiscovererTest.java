package sneer.pulp.dyndns.ownip.tests;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.brickness.testsupport.Contribute;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.clock.Clock;
import sneer.pulp.dyndns.checkip.CheckIp;
import sneer.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.pulp.propertystore.PropertyStore;
import sneer.pulp.propertystore.mocks.TransientPropertyStore;
import wheel.reactive.impl.EventReceiver;

public class OwnIpDiscovererTest extends BrickTest {
	
	@Contribute final CheckIp checkip = mock(CheckIp.class);
	@Contribute final PropertyStore store = new TransientPropertyStore();
	
	@Test
	public void testDiscovery() throws IOException {
		final Consumer<String> receiver = mock(Consumer.class);
		final String ip1 = "123.45.67.89";
		final String ip2 = "12.34.56.78";

		int retryTime = 11 * 60 * 1000;

		checking(new Expectations() {{
			final Sequence seq = sequence("sequence");
			one(receiver).consume(null); inSequence(seq);

			one(checkip).check(); will(returnValue(ip1)); inSequence(seq);
			one(receiver).consume(ip1); inSequence(seq);
			
			one(checkip).check(); will(returnValue(ip1)); inSequence(seq);
			
			one(checkip).check(); will(returnValue(ip2)); inSequence(seq);
			one(receiver).consume(ip2); inSequence(seq);
			
			one(checkip).check(); will(returnValue(ip2)); inSequence(seq);

		}});
		
		OwnIpDiscoverer subject = my(OwnIpDiscoverer.class);
		
		@SuppressWarnings("unused")
		final EventReceiver<String> refToAvoidGc = new EventReceiver<String>(subject.ownIp()) { @Override public void consume(String value) {
			receiver.consume(value);
		}};
		
		Clock clock = my(Clock.class);
		clock.advanceTime(0);
		clock.advanceTime(retryTime);
		clock.advanceTime(retryTime);
		clock.advanceTime(retryTime);
	}
}