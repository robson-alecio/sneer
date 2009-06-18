package sneer.bricks.pulp.dyndns.ownip.tests;

import static sneer.foundation.environments.Environments.my;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.junit.Test;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.pulp.dyndns.checkip.CheckIp;
import sneer.bricks.pulp.dyndns.ownip.OwnIpDiscoverer;
import sneer.bricks.pulp.propertystore.PropertyStore;
import sneer.bricks.pulp.propertystore.mocks.TransientPropertyStore;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.Contribute;
import sneer.foundation.lang.Consumer;

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

		@SuppressWarnings("unused") final Object referenceToAvoidGc = my(Signals.class).receive(subject.ownIp(), new Consumer<String>() { @Override public void consume(String value) {
			receiver.consume(value);
		}});
		

		Clock clock = my(Clock.class);
		clock.advanceTime(0);
		clock.advanceTime(retryTime);
		clock.advanceTime(retryTime);
		clock.advanceTime(retryTime);
	}
}