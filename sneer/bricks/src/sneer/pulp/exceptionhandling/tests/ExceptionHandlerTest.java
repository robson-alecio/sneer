package sneer.pulp.exceptionhandling.tests;

import static wheel.lang.Environments.my;

import org.junit.Test;

import sneer.pulp.exceptionhandling.ExceptionHandler;
import tests.TestInContainerEnvironment;
import wheel.lang.ByRef;

public class ExceptionHandlerTest extends TestInContainerEnvironment {

	private ExceptionHandler _handler = my(ExceptionHandler.class);
	
	@Test
	public void testShielding() {
		final ByRef<Boolean> executed = ByRef.newInstance(false);
		_handler.shield(new Runnable() { @Override public void run() {
			executed.value = true;
			throw new Error();
		}});
		assertTrue(executed.value);
	}
}
