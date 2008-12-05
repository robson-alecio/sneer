package sneer.pulp.exceptionhandling.tests;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.runner.RunWith;

import sneer.pulp.exceptionhandling.ExceptionHandler;
import tests.Contribute;
import tests.JMockContainerEnvironment;
import tests.TestThatIsInjected;
import wheel.lang.Fallible;

@RunWith(JMockContainerEnvironment.class)
public abstract class ShieldingTestBase extends TestThatIsInjected {

	protected final Mockery _mockery = new JUnit4Mockery();
	
	@Contribute protected final ExceptionHandler _handlerMock = _mockery.mock(ExceptionHandler.class);
	
	{
		_mockery.checking(new Expectations() {{
			one(_handlerMock).shield(with(aNonNull(Fallible.class)));
		}});
	}
}