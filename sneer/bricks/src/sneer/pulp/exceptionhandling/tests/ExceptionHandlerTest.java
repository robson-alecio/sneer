package sneer.pulp.exceptionhandling.tests;

import org.junit.Test;

import sneer.pulp.exceptionhandling.ExceptionHandler;
import tests.TestThatIsInjected;
import wheel.lang.ByRef;
import wheel.lang.Fallible;
import static wheel.lang.Environments.my;

public class ExceptionHandlerTest extends TestThatIsInjected {

	private ExceptionHandler _handler = my(ExceptionHandler.class);
	
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
