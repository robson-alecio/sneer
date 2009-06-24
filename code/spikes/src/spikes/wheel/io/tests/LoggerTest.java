package spikes.wheel.io.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Test;

import sneer.bricks.hardware.io.log.Logger;
import sneer.bricks.hardware.io.log.workers.notifier.LogNotifier;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.lang.ByRef;
import sneer.foundation.lang.Consumer;


public class LoggerTest extends BrickTest {

	@SuppressWarnings("unused")	private Object _referenceToAvoidGc;

	@Test
	public void insets() {
		assertLog(
			"User Peter is not allowed to access the TPS report.",
			"User {} is not allowed to access the {} report.", "Peter", "TPS"
		);
	}

	@Test
	public void omittingLastBrackets() {
		assertLog(
			"User Peter is not allowed to access report: TPS",
			"User {} is not allowed to access report: ", "Peter", "TPS"
		);
	}

	@Test
	public void noBrackets() {
		assertLog(
			"User is not allowed to access report: TPS",
			"User is not allowed to access report: ", "TPS"
		);
	}

	@Test
	public void noInsets() {
		assertLog(
			"Hello world",
			"Hello world"
		);
	}

	private void assertLog(String expected, String message, Object... insets) {
		final ByRef<String> observed = ByRef.newInstance();
		_referenceToAvoidGc = my(Signals.class).receive(my(LogNotifier.class).loggedMessages(), new Consumer<String>() { @Override public void consume(String msg) {
			observed.value = msg;
		}});

		my(Logger.class).log(message, insets);
		
		assertTrue(observed.value.indexOf(expected) != -1);
	}
}
