package wheel.io.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.commons.lang.ByRef;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.log.Logger;
import sneer.pulp.log.workers.notifier.LogNotifier;
import sneer.pulp.reactive.Signals;


public class LoggerTest extends BrickTest {

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
		my(Signals.class).receive(this, new Consumer<String>() { @Override public void consume(String msg) {
			observed.value = msg;
		}},my(LogNotifier.class).loggedMessages());

		my(Logger.class).log(message, insets);
		
		assertTrue(observed.value.indexOf(expected) != -1);
	}


}
