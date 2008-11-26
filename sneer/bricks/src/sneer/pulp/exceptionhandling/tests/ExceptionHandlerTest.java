package sneer.pulp.exceptionhandling.tests;

import org.junit.Test;

import sneer.kernel.container.Inject;
import sneer.pulp.exceptionhandling.ExceptionHandler;
import sneer.pulp.exceptionhandling.Fallible;
import tests.TestThatIsInjected;
import wheel.lang.ByRef;

public class ExceptionHandlerTest extends TestThatIsInjected {

	@Inject private static ExceptionHandler _handler;
	
	@Test
	public void testShielding() {
		final ByRef<Boolean> executed = ByRef.newInstance(false);
		_handler.shield(new Fallible() { @Override public void run() throws Throwable {
			executed.value = true;
			throw new Throwable();
		}});
		assertTrue(executed.value);
	}
}
