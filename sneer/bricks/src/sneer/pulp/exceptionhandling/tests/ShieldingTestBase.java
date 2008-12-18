package sneer.pulp.exceptionhandling.tests;

import org.jmock.Expectations;

import sneer.pulp.exceptionhandling.ExceptionHandler;
import tests.Contribute;
import tests.TestInContainerEnvironment;

public abstract class ShieldingTestBase extends TestInContainerEnvironment {

	@Contribute protected final ExceptionHandler _handlerMock = mock(ExceptionHandler.class);
	
	{
		checking(new Expectations() {{
			one(_handlerMock).shield(with(aNonNull(Runnable.class)));
		}});
	}
}