package sneer.bricks.pulp.events.pulsers.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.pulp.events.pulsers.Pulser;
import sneer.bricks.pulp.events.pulsers.Pulsers;
import sneer.foundation.brickness.testsupport.BrickTest;

public class PulsersTest extends BrickTest {

	private final Pulsers _subject = my(Pulsers.class);
	private int _counter = 0;

	@Test
	public void receiveFromSeveralPulseSources() {
		Pulser p1 = my(Pulsers.class).newInstance();
		Pulser p2 = my(Pulsers.class).newInstance();
		Pulser p3 = my(Pulsers.class).newInstance();
		
		@SuppressWarnings("unused")
		WeakContract contract = _subject.receive(new Runnable() { @Override public void run() {
			_counter++;
		}}, p1.output(), p2.output(), p3.output());
		
		p1.sendPulse();
		p2.sendPulse();
		p3.sendPulse();
		
		assertSame(3, _counter);
	}

}
